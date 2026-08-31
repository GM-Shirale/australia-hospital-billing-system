package com.hospital.hospital_billing_system.patient.repository;

import com.hospital.hospital_billing_system.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByMedicareNumber(String medicareNumber);

}