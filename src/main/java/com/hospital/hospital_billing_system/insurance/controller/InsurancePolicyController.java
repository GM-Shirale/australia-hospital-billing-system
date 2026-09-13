package com.hospital.hospital_billing_system.insurance.controller;

import com.hospital.hospital_billing_system.insurance.dto.InsurancePolicyRequest;
import com.hospital.hospital_billing_system.insurance.dto.InsurancePolicyResponse;
import com.hospital.hospital_billing_system.insurance.service.InsurancePolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/insurance/policies")
@RequiredArgsConstructor
public class InsurancePolicyController {

    private final InsurancePolicyService policyService;

    public InsurancePolicyController(InsurancePolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<InsurancePolicyResponse> createPolicy(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @Valid @RequestBody InsurancePolicyRequest request) {
        InsurancePolicyResponse response = policyService.createPolicy(request, tenantId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{policyId}")
    public ResponseEntity<InsurancePolicyResponse> getPolicyById(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID policyId) {
        return ResponseEntity.ok(policyService.getPolicyById(policyId, tenantId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<InsurancePolicyResponse>> getPoliciesByPatient(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @PathVariable UUID patientId) {
        return ResponseEntity.ok(policyService.getPoliciesByPatient(patientId, tenantId));
    }
}