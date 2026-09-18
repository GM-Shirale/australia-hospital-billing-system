package com.hospital.hospital_billing_system.insurance.controller;

import com.hospital.hospital_billing_system.insurance.dto.ClaimResponse;
import com.hospital.hospital_billing_system.insurance.dto.ClaimSubmissionRequest;
import com.hospital.hospital_billing_system.insurance.service.InsuranceClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/insurance/claims")
@RequiredArgsConstructor
public class InsuranceClaimController {

    private final InsuranceClaimService claimService;

    @PostMapping("/adjudicate")
    public ResponseEntity<ClaimResponse> adjudicateClaim(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @Valid @RequestBody ClaimSubmissionRequest request) {
        ClaimResponse response = claimService.submitAndAdjudicateClaim(request, tenantId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<ClaimResponse> getClaimById(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID claimId) {
        return ResponseEntity.ok(claimService.getClaimById(claimId, tenantId));
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<ClaimResponse> getClaimByBillId(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID billId) {
        return ResponseEntity.ok(claimService.getClaimByBillId(billId, tenantId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByPatient(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID patientId) {
        return ResponseEntity.ok(claimService.getClaimsByPatient(patientId, tenantId));
    }
}