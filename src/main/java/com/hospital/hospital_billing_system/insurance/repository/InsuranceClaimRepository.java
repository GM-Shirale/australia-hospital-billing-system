package com.hospital.hospital_billing_system.insurance.repository;

import com.hospital.hospital_billing_system.insurance.entity.InsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, UUID> {

    Optional<InsuranceClaim> findByIdAndTenantId(UUID id, UUID tenantId);

    List<InsuranceClaim> findByPatientIdAndTenantId(UUID patientId, UUID tenantId);

    Optional<InsuranceClaim> findByBillIdAndTenantId(UUID billId, UUID tenantId);
}