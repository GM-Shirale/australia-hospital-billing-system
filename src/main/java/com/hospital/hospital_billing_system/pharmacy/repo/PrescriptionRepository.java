package com.hospital.hospital_billing_system.pharmacy.repo;

import com.hospital.hospital_billing_system.common.enums.PrescriptionStatus;
import com.hospital.hospital_billing_system.pharmacy.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {

    List<Prescription> findByPatientPatientId(Long patientId);

    List<Prescription> findByDoctorDoctorId(UUID doctorId);

    List<Prescription> findByStatus(PrescriptionStatus status);

    List<Prescription> findByPrescriptionDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
