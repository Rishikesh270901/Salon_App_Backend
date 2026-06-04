package com.Rishikesh.PaymentService.service;

import com.Rishikesh.PaymentService.domain.PaymentMethod;
import com.Rishikesh.PaymentService.modal.PaymentOrder;
import com.Rishikesh.PaymentService.payload.response.PaymentLinkResponse;
import com.Rishikesh.PaymentService.payload.response.dto.BookingDTO;
import com.Rishikesh.PaymentService.payload.response.dto.UserDTO;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentLinkResponse createOrder(UserDTO userDTO, BookingDTO bookingDTO, PaymentMethod paymentMethod) throws RazorpayException, StripeException;

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

    PaymentLink createRazorpayPaymentLink(UserDTO userDTO, Long amount, Long orderId) throws RazorpayException;

    String createStripePaymentLink(UserDTO userDTO, Long amount, Long orderId) throws StripeException;

    Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;
}
