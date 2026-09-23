package com.hospital.hospital_billing_system.billing.controller;

import com.hospital.hospital_billing_system.billing.dto.PaymentRequest;
import com.hospital.hospital_billing_system.billing.dto.PaymentResponse;
import com.hospital.hospital_billing_system.billing.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody PaymentRequest request) {

        PaymentResponse response =
                paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(paymentId)
        );
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<PaymentResponse> getPaymentByBillId(
            @PathVariable Long billId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByBillId(billId)
        );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }
}