package com.hospital.hospital_billing_system.billing.controller;

import com.hospital.hospital_billing_system.billing.dto.RefundRequest;
import com.hospital.hospital_billing_system.billing.dto.RefundResponse;
import com.hospital.hospital_billing_system.billing.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundResponse> createRefund(
            @RequestBody RefundRequest request) {

        RefundResponse response = refundService.createRefund(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{refundId}")
    public ResponseEntity<RefundResponse> getRefundById(
            @PathVariable Long refundId) {

        return ResponseEntity.ok(
                refundService.getRefundById(refundId)
        );
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<RefundResponse>> getRefundsByTransactionId(
            @PathVariable Long transactionId) {

        return ResponseEntity.ok(
                refundService.getRefundsByTransactionId(transactionId)
        );
    }

    @GetMapping
    public ResponseEntity<List<RefundResponse>> getAllRefunds() {

        return ResponseEntity.ok(
                refundService.getAllRefunds()
        );
    }
}