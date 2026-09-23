package com.hospital.hospital_billing_system.admission.repository;

import com.hospital.hospital_billing_system.admission.entity.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission,Long> {

    // get next admission number from PostgreSQL sequence
    @Query(value = "SELECT nextval('admission_number_seq')", nativeQuery = true)
    long getNextAdmissionNumber();

    // get all admissions for a patient
    List<Admission> findByPatientPatientId(Long patientId);
}
