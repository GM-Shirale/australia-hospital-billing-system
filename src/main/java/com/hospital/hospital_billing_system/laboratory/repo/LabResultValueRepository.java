package com.hospital.hospital_billing_system.laboratory.repo;


import com.hospital.hospital_billing_system.laboratory.entity.LabResultValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabResultValueRepository extends JpaRepository<LabResultValue, Long> {
}
