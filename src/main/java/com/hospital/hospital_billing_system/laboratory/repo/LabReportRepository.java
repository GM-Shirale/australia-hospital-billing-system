package com.hospital.hospital_billing_system.laboratory.repo;


import com.hospital.hospital_billing_system.common.enums.ReportStatus;
import com.hospital.hospital_billing_system.laboratory.entity.LabReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabReportRepository extends JpaRepository<LabReport, Long> {
    Optional<LabReport> findByReportNumber(String reportNumber);

    boolean existsByReportNumber(String reportNumber);

    List<LabReport> findByLabOrderId(Long labOrderId);

    Optional<LabReport> findByVerificationId(Long verificationId);

    List<LabReport> findByPatientId(Long patientId);

    List<LabReport> findByStatus(ReportStatus status);
}