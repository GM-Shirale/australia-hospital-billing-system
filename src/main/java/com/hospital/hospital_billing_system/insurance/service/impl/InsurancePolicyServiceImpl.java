package com.hospital.hospital_billing_system.insurance.service.impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.insurance.dto.InsurancePolicyRequest;
import com.hospital.hospital_billing_system.insurance.dto.InsurancePolicyResponse;
import com.hospital.hospital_billing_system.insurance.entity.InsurancePolicy;
import com.hospital.hospital_billing_system.insurance.repository.InsurancePolicyRepository;
import com.hospital.hospital_billing_system.insurance.service.InsurancePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    private final InsurancePolicyRepository policyRepository;

    @Override
    public InsurancePolicyResponse createPolicy(InsurancePolicyRequest request, UUID tenantId) {
        // Business Rule 1: Validate policy start and end dates
        if (request.getValidTo().isBefore(request.getValidFrom())) {
            throw new IllegalArgumentException("Policy expiry date (validTo) cannot be before start date (validFrom)");
        }

        // Business Rule 2: Prevent duplicate policy number within the same tenant
        if (policyRepository.existsByPolicyNumberAndTenantId(request.getPolicyNumber(), tenantId)) {
            throw new DuplicateResourceException("Insurance policy with number " + request.getPolicyNumber() + " already exists");
        }

        InsurancePolicy policy = InsurancePolicy.builder()
                .tenantId(tenantId)
                .patientId(request.getPatientId())
                .policyNumber(request.getPolicyNumber())
                .providerName(request.getProviderName())
                .insuranceType(request.getInsuranceType())
                .coverageLimit(request.getCoverageLimit())
                .remainingLimit(request.getCoverageLimit()) // initially, remaining balance equals full limit
                .validFrom(request.getValidFrom())
                .validTo(request.getValidTo())
                .active(true)
                .build();

        InsurancePolicy savedPolicy = policyRepository.save(policy);
        return mapToResponse(savedPolicy);
    }

    @Override
    @Transactional(readOnly = true)
    public InsurancePolicyResponse getPolicyById(UUID policyId, UUID tenantId) {
        InsurancePolicy policy = policyRepository.findByIdAndTenantId(policyId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance policy not found with ID: " + policyId));
        return mapToResponse(policy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsurancePolicyResponse> getPoliciesByPatient(UUID patientId, UUID tenantId) {
        return policyRepository.findByPatientIdAndTenantId(patientId, tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InsurancePolicyResponse mapToResponse(InsurancePolicy policy) {
        return InsurancePolicyResponse.builder()
                .id(policy.getId())
                .tenantId(policy.getTenantId())
                .patientId(policy.getPatientId())
                .policyNumber(policy.getPolicyNumber())
                .providerName(policy.getProviderName())
                .insuranceType(policy.getInsuranceType())
                .coverageLimit(policy.getCoverageLimit())
                .remainingLimit(policy.getRemainingLimit())
                .validFrom(policy.getValidFrom())
                .validTo(policy.getValidTo())
                .active(policy.isActive())
                .build();
    }
}