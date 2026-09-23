package com.hospital.hospital_billing_system.admission.service;

import com.hospital.hospital_billing_system.admission.dto.AdmissionRequest;
import com.hospital.hospital_billing_system.admission.dto.AdmissionResponse;

import java.util.List;

public interface AdmissionService {

    // create admission for patient
    AdmissionResponse createAdmission(Long patientId, AdmissionRequest request);

    // get admission by id
    AdmissionResponse getAdmissionById(Long admissionId);

    // get all admissions
    List<AdmissionResponse> getAllAdmissions();

    // get all admissions of a patient
    List<AdmissionResponse> getAdmissionsByPatientId(Long patientId);

    // update admission
    AdmissionResponse updateAdmission(Long admissionId, AdmissionRequest request);

    // discharge patient
    AdmissionResponse dischargeAdmission(Long admissionId);

    // delete admission
    void deleteAdmission(Long admissionId);
}