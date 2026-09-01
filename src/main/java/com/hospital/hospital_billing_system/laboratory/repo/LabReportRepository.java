package com.hospital.hospital_billing_system.laboratory.repo;


import com.hospital.hospital_billing_system.laboratory.entity.LabReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabReportRepository extends JpaRepository<LabReport, Long> {
}