package com.hospital.hospital_billing_system.patient.service;

import com.hospital.hospital_billing_system.patient.dto.PatientRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientResponse;

import java.util.List;

public interface PatientService {

    // create new patient
    PatientResponse createPatient(PatientRequest request);

    // get patient by id
    PatientResponse getPatientById(Long patientId);

    // get all patients
    List<PatientResponse> getAllPatients();

    // update patient
    PatientResponse updatePatient(Long patientId, PatientRequest request);

    // delete patient
    void deletePatient(Long patientId);
}