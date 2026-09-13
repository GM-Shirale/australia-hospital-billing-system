package com.hospital.hospital_billing_system.insurance.service.impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.insurance.dto.ClaimResponse;
import com.hospital.hospital_billing_system.insurance.dto.ClaimSubmissionRequest;
import com.hospital.hospital_billing_system.insurance.entity.InsuranceClaim;
import com.hospital.hospital_billing_system.insurance.entity.InsurancePolicy;
import com.hospital.hospital_billing_system.insurance.enums.ClaimStatus;
import com.hospital.hospital_billing_system.insurance.repository.InsuranceClaimRepository;
import com.hospital.hospital_billing_system.insurance.repository.InsurancePolicyRepository;
import com.hospital.hospital_billing_system.insurance.service.InsuranceClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InsuranceClaimServiceImpl implements InsuranceClaimService {

    private final InsuranceClaimRepository claimRepository;
    private final InsurancePolicyRepository policyRepository;

    @Override
    public ClaimResponse submitAndAdjudicateClaim(ClaimSubmissionRequest request, UUID tenantId) {
        // 1. Prevent duplicate claims for the same bill within the tenant
        claimRepository.findByBillIdAndTenantId(request.getBillId(), tenantId).ifPresent(c -> {
            throw new DuplicateResourceException("An insurance claim already exists for bill ID: " + request.getBillId());
        });

        // 2. Fetch and validate policy ownership under this tenant
        InsurancePolicy policy = policyRepository.findByIdAndTenantId(request.getPolicyId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance policy not found with ID: " + request.getPolicyId()));

        if (!policy.getPatientId().equals(request.getPatientId())) {
            throw new IllegalArgumentException("Policy does not belong to patient ID: " + request.getPatientId());
        }

        BigDecimal claimedAmount = request.getClaimedAmount();
        BigDecimal approvedAmount = BigDecimal.ZERO;
        BigDecimal coPayment = claimedAmount;
        ClaimStatus status;
        String rejectionReason = null;

        // 3. Adjudication Rules: Date Validity & Active Status
        boolean isDateValid = !request.getServiceDate().isBefore(policy.getValidFrom())
                && !request.getServiceDate().isAfter(policy.getValidTo());

        if (!policy.isActive()) {
            status = ClaimStatus.REJECTED;
            rejectionReason = "Policy is inactive";
        } else if (!isDateValid) {
            status = ClaimStatus.REJECTED;
            rejectionReason = "Service date is outside policy validity period";
        } else if (policy.getRemainingLimit().compareTo(BigDecimal.ZERO) <= 0) {
            status = ClaimStatus.REJECTED;
            rejectionReason = "Annual coverage limit exhausted";
        } else {
            // 4. Coverage Limit Calculation (Full vs Partial Approval)
            if (policy.getRemainingLimit().compareTo(claimedAmount) >= 0) {
                approvedAmount = claimedAmount;
                coPayment = BigDecimal.ZERO;
                status = ClaimStatus.APPROVED;
                policy.setRemainingLimit(policy.getRemainingLimit().subtract(approvedAmount));
            } else {
                approvedAmount = policy.getRemainingLimit();
                coPayment = claimedAmount.subtract(approvedAmount);
                status = ClaimStatus.PARTIALLY_APPROVED;
                policy.setRemainingLimit(BigDecimal.ZERO);
            }
            policyRepository.save(policy);
        }

        InsuranceClaim claim = InsuranceClaim.builder()
                .tenantId(tenantId)
                .billId(request.getBillId())
                .patientId(request.getPatientId())
                .policy(policy)
                .claimedAmount(claimedAmount)
                .approvedAmount(approvedAmount)
                .patientCoPayment(coPayment)
                .status(status)
                .rejectionReason(rejectionReason)
                .claimDate(LocalDateTime.now())
                .settlementDate(status == ClaimStatus.APPROVED || status == ClaimStatus.PARTIALLY_APPROVED ? LocalDateTime.now() : null)
                .build();

        InsuranceClaim savedClaim = claimRepository.save(claim);
        return mapToResponse(savedClaim);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimResponse getClaimById(UUID claimId, UUID tenantId) {
        InsuranceClaim claim = claimRepository.findByIdAndTenantId(claimId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found with ID: " + claimId));
        return mapToResponse(claim);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaimResponse> getClaimsByPatient(UUID patientId, UUID tenantId) {
        return claimRepository.findByPatientIdAndTenantId(patientId, tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimResponse getClaimByBillId(UUID billId, UUID tenantId) {
        InsuranceClaim claim = claimRepository.findByBillIdAndTenantId(billId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found for bill ID: " + billId));
        return mapToResponse(claim);
    }

    private ClaimResponse mapToResponse(InsuranceClaim claim) {
        return ClaimResponse.builder()
                .claimId(claim.getId())
                .billId(claim.getBillId())
                .patientId(claim.getPatientId())
                .policyId(claim.getPolicy().getId())
                .policyNumber(claim.getPolicy().getPolicyNumber())
                .providerName(claim.getPolicy().getProviderName())
                .claimedAmount(claim.getClaimedAmount())
                .approvedAmount(claim.getApprovedAmount())
                .patientCoPayment(claim.getPatientCoPayment())
                .status(claim.getStatus())
                .rejectionReason(claim.getRejectionReason())
                .claimDate(claim.getClaimDate())
                .settlementDate(claim.getSettlementDate())
                .build();
    }
}