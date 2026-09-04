package com.hospital.hospital_billing_system.patient.controller;


import com.hospital.hospital_billing_system.patient.dto.PatientDocumentRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientDocumentResponse;
import com.hospital.hospital_billing_system.patient.dto.PatientDocumentUploadRequest;
import com.hospital.hospital_billing_system.patient.service.PatientDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientDocumentController {

    private final PatientDocumentService patientDocumentService;

    // constructor injection
    public PatientDocumentController(
            PatientDocumentService patientDocumentService) {
        this.patientDocumentService = patientDocumentService;
    }

    // upload multiple documents for patient
    @PostMapping(
            value = "/{patientId}/documents",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<List<PatientDocumentResponse>> uploadDocuments(
            @PathVariable Long patientId,
            @RequestPart("documents")
            List<PatientDocumentUploadRequest> requests,
            @RequestPart("files")
            MultipartFile[] files) {

        List<PatientDocumentResponse> response =
                patientDocumentService.uploadDocuments(
                        patientId,
                        requests,
                        files
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // get document by id
    @GetMapping("/documents/{documentId}")
    public ResponseEntity<PatientDocumentResponse> getDocumentById(
            @PathVariable Long documentId) {

        return ResponseEntity.ok(
                patientDocumentService.getDocumentById(documentId)
        );
    }

    // get all documents of patient
    @GetMapping("/{patientId}/documents")
    public ResponseEntity<List<PatientDocumentResponse>> getDocumentsByPatientId(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                patientDocumentService.getDocumentsByPatientId(patientId)
        );
    }

    // update document details
    @PutMapping("/documents/{documentId}")
    public ResponseEntity<PatientDocumentResponse> updateDocument(
            @PathVariable Long documentId,
            @RequestBody PatientDocumentRequest request) {

        return ResponseEntity.ok(
                patientDocumentService.updateDocument(
                        documentId,
                        request
                )
        );
    }

    // delete document
    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long documentId) {

        patientDocumentService.deleteDocument(documentId);

        return ResponseEntity.noContent().build();
    }
}