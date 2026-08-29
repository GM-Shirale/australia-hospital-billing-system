package com.hospital.hospital_billing_system.patient.repository;

import com.hospital.hospital_billing_system.patient.entity.PatientAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientAddressRepository extends JpaRepository<PatientAddress, Long> {

}