package com.hospital.hospital_billing_system.patient.repository;


import com.hospital.hospital_billing_system.patient.entity.PatientDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientDocumentRepository extends JpaRepository<PatientDocument, Long> {

    // find all documents of a patient
    List<PatientDocument> findByPatientPatientId(Long patientId);

}
