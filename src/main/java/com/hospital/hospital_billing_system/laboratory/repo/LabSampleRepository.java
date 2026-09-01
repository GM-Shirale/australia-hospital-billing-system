package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabSample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabSampleRepository extends JpaRepository<LabSample, Long> {
}