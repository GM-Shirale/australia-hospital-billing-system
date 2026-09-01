package com.hospital.hospital_billing_system.laboratory.repo;


import com.hospital.hospital_billing_system.laboratory.entity.LabCharge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabChargeRepository extends JpaRepository<LabCharge, Long> {
}