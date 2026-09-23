package com.hospital.hospital_billing_system.insurance.repository;

import com.hospital.hospital_billing_system.insurance.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, UUID> {

    Optional<InsurancePolicy> findByIdAndTenantId(UUID id, UUID tenantId);

    Optional<InsurancePolicy> findByPolicyNumberAndTenantId(String policyNumber, UUID tenantId);

    List<InsurancePolicy> findByPatientIdAndTenantId(UUID patientId, UUID tenantId);

    boolean existsByPolicyNumberAndTenantId(String policyNumber, UUID tenantId);
}