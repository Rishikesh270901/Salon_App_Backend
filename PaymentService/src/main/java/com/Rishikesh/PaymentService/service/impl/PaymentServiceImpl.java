package com.Rishikesh.PaymentService.service.impl;

import com.Rishikesh.PaymentService.domain.PaymentMethod;
import com.Rishikesh.PaymentService.domain.PaymentOrderStatus;
import com.Rishikesh.PaymentService.modal.PaymentOrder;
import com.Rishikesh.PaymentService.payload.response.PaymentLinkResponse;
import com.Rishikesh.PaymentService.payload.response.dto.PaymentLinkDTO;
import com.Rishikesh.PaymentService.payload.response.dto.BookingDTO;
import com.Rishikesh.PaymentService.payload.response.dto.UserDTO;
import com.Rishikesh.PaymentService.repository.PaymentOrderRespository;
import com.Rishikesh.PaymentService.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRespository paymentOrderRespository;

    @Value("${stripe.api.key}")
    private String stripeSecrectKey;

    @Value("${razorpay.api.key}")
    private String razorpayAPIKey;

    @Value("${razorpay.api.secret}")
    private String razorpayAPISecret;

    @Override
    public PaymentLinkResponse createOrder(UserDTO userDTO, BookingDTO bookingDTO, PaymentMethod paymentMethod) throws RazorpayException, StripeException {

        Long amount = (long) bookingDTO.getTotalPrice();
        PaymentOrder paymentOrder = new PaymentOrder();

        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setBookingId(bookingDTO.getId());
        paymentOrder.setSalonId(bookingDTO.getSalonId());

        PaymentOrder savedOrder = paymentOrderRespository.save(paymentOrder);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink payment = createRazorpayPaymentLink(userDTO, savedOrder.getAmount(), savedOrder.getId());

            String paymentURL = payment.get("short_url");
            String paymentURLID = payment.get("id");
            paymentLinkResponse.setPaymentLinkURL(paymentURL);
            paymentLinkResponse.setPaymentLinkId(paymentURLID);
            savedOrder.setPaymentLinkId(paymentURLID);

            paymentOrderRespository.save(savedOrder);
        }

        else{
            String paymentURL = createStripePaymentLink(userDTO, savedOrder.getAmount(), savedOrder.getId());
            paymentLinkResponse.setPaymentLinkURL(paymentURL);
        }

        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRespository.findById(id).orElse(null);
        if(paymentOrder==null){
            throw new Exception("Payment order not found");
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return paymentOrderRespository.findByPaymentLinkId(paymentId);
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(UserDTO userDTO, Long Amount, Long orderId) throws RazorpayException {

        Long amount = Amount*100;

        RazorpayClient razorpay = new RazorpayClient(razorpayAPIKey, razorpayAPISecret);

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount);
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name",userDTO.getFullname());
        customer.put("email",userDTO.getEmail());
        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);
        paymentLinkRequest.put("notify", notify);

        paymentLinkRequest.put("remainder_enable", true);

        paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success"+orderId);

        paymentLinkRequest.put("callback_method", "get");

        return razorpay.paymentLink.create(paymentLinkRequest);
    }

    @Override
    public String createStripePaymentLink(UserDTO userDTO, Long amount, Long orderId) throws StripeException {

        Stripe.apiKey = stripeSecrectKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success"+orderId)
                .setCancelUrl("http://localhost:3000/payment/cancelled")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("USD")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Salon Appointment Booking")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);

       return session.getUrl();

    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {

        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpay = new RazorpayClient(razorpayAPIKey, razorpayAPIKey);

                Payment payment = razorpay.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");
                if(status.equals("captures")){
//                    Produce Kafka event
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRespository.save(paymentOrder);
                    return true;
                }
                return false;
            }
            else{
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRespository.save(paymentOrder);
                return true;
            }
        }

        return false;
    }
}
