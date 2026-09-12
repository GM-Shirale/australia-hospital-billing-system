package com.hospital.hospital_billing_system.billing.controller;

import com.hospital.hospital_billing_system.billing.dto.BillingSummaryResponse;
import com.hospital.hospital_billing_system.billing.service.BillingSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing-summary")
@RequiredArgsConstructor
public class BillingSummaryController {

    private final BillingSummaryService billingSummaryService;

    // Hospital/admin billing summary
    @GetMapping
    public ResponseEntity<BillingSummaryResponse> getBillingSummary() {

        return ResponseEntity.ok(
                billingSummaryService.getBillingSummary()
        );
    }

    // Patient billing summary
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<BillingSummaryResponse> getPatientBillingSummary(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                billingSummaryService.getBillingSummary(patientId)
        );
    }
}