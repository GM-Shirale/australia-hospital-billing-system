package com.hospital.hospital_billing_system.insurance.service;

import com.hospital.hospital_billing_system.insurance.dto.ClaimResponse;
import com.hospital.hospital_billing_system.insurance.dto.ClaimSubmissionRequest;

import java.util.List;
import java.util.UUID;

public interface InsuranceClaimService {

    ClaimResponse submitAndAdjudicateClaim(ClaimSubmissionRequest request, UUID tenantId);

    ClaimResponse getClaimById(UUID claimId, UUID tenantId);

    List<ClaimResponse> getClaimsByPatient(UUID patientId, UUID tenantId);

    ClaimResponse getClaimByBillId(UUID billId, UUID tenantId);
}