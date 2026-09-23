package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import com.hospital.hospital_billing_system.laboratory.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification,Long> {


    Optional<Verification> findByLabResultId(Long labResultId);
    List<Verification> findByVerifiedBy(Long verifiedBy);
    List<Verification> findByStatus(VerificationStatus status);
}
