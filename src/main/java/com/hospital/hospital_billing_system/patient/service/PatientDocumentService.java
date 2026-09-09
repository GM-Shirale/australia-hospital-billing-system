package com.hospital.hospital_billing_system.patient.service;

import com.hospital.hospital_billing_system.patient.dto.PatientDocumentRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientDocumentResponse;
import com.hospital.hospital_billing_system.patient.dto.PatientDocumentUploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PatientDocumentService {

    // upload multiple documents for patient
    List<PatientDocumentResponse> uploadDocuments(
            Long patientId,
            List<PatientDocumentUploadRequest> requests,
            MultipartFile[] files
    );

    // get document by id
    PatientDocumentResponse getDocumentById(Long documentId);

    // get all documents of patient
    List<PatientDocumentResponse> getDocumentsByPatientId(Long patientId);

    // update document details
    PatientDocumentResponse updateDocument(
            Long documentId,
            PatientDocumentRequest request
    );

    // delete document
    void deleteDocument(Long documentId);
}