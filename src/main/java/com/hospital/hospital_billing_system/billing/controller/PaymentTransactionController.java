package com.hospital.hospital_billing_system.billing.controller;

import com.hospital.hospital_billing_system.billing.dto.PaymentTransactionRequest;
import com.hospital.hospital_billing_system.billing.dto.PaymentTransactionResponse;
import com.hospital.hospital_billing_system.billing.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-transactions")
@RequiredArgsConstructor
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    @PostMapping
    public ResponseEntity<PaymentTransactionResponse> createTransaction(
            @RequestBody PaymentTransactionRequest request) {

        PaymentTransactionResponse response =
                paymentTransactionService.createTransaction(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PaymentTransactionResponse> getTransactionById(
            @PathVariable Long transactionId) {

        return ResponseEntity.ok(
                paymentTransactionService.getTransactionById(transactionId)
        );
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<PaymentTransactionResponse>> getTransactionsByPaymentId(
            @PathVariable Long paymentId) {

        return ResponseEntity.ok(
                paymentTransactionService.getTransactionsByPaymentId(paymentId)
        );
    }
}