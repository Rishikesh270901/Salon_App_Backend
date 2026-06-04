package com.Rishikesh.PaymentService.controller;


import com.Rishikesh.PaymentService.domain.PaymentMethod;
import com.Rishikesh.PaymentService.modal.PaymentOrder;
import com.Rishikesh.PaymentService.payload.response.PaymentLinkResponse;
import com.Rishikesh.PaymentService.payload.response.dto.BookingDTO;
import com.Rishikesh.PaymentService.payload.response.dto.UserDTO;
import com.Rishikesh.PaymentService.service.PaymentService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@RequestBody BookingDTO booking,
                                                                 @RequestParam PaymentMethod paymentMethod) throws StripeException, RazorpayException {
        UserDTO user = new UserDTO();
        user.setId(1L); // Mock user ID, replace with actual user retrieval logic
        user.setFullname("Rishikesh"); // Mock user name, replace with actual user retrieval logic
        user.setEmail("rishik@gmail.com"); // Mock user email, replace with actual user retrieval logic

        PaymentLinkResponse response = paymentService.createOrder(user, booking, paymentMethod);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(@PathVariable Long id) throws Exception {

        PaymentOrder response = paymentService.getPaymentOrderById(id);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(@RequestParam String paymentId,
                                                       @RequestParam String paymentLinkId) throws Exception {

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        Boolean response = paymentService.proceedPayment(paymentOrder, paymentId, paymentLinkId);

        return ResponseEntity.ok(response);
    }
}
