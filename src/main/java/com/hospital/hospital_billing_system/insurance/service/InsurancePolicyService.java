package com.hospital.hospital_billing_system.insurance.service;

import com.hospital.hospital_billing_system.insurance.dto.InsurancePolicyRequest;
import com.hospital.hospital_billing_system.insurance.dto.InsurancePolicyResponse;

import java.util.List;
import java.util.UUID;

public interface InsurancePolicyService {

    InsurancePolicyResponse createPolicy(InsurancePolicyRequest request, UUID tenantId);

    InsurancePolicyResponse getPolicyById(UUID policyId, UUID tenantId);

    List<InsurancePolicyResponse> getPoliciesByPatient(UUID patientId, UUID tenantId);
}