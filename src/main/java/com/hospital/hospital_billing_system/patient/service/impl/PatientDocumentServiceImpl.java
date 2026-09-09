package com.hospital.hospital_billing_system.patient.service.impl;

import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.patient.dto.PatientDocumentRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientDocumentResponse;
import com.hospital.hospital_billing_system.patient.dto.PatientDocumentUploadRequest;
import com.hospital.hospital_billing_system.patient.entity.Patient;
import com.hospital.hospital_billing_system.patient.entity.PatientDocument;
import com.hospital.hospital_billing_system.patient.repository.PatientDocumentRepository;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import com.hospital.hospital_billing_system.patient.service.FileStorageService;
import com.hospital.hospital_billing_system.patient.service.PatientDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientDocumentServiceImpl implements PatientDocumentService {

    private static final Logger log =
            LoggerFactory.getLogger(PatientDocumentServiceImpl.class);

    private final PatientDocumentRepository patientDocumentRepository;
    private final PatientRepository patientRepository;
    private final FileStorageService fileStorageService;

    // constructor injection

    public PatientDocumentServiceImpl(
            PatientDocumentRepository patientDocumentRepository,
            PatientRepository patientRepository,
            FileStorageService fileStorageService) {

        this.patientDocumentRepository = patientDocumentRepository;
        this.patientRepository = patientRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<PatientDocumentResponse> uploadDocuments(
            Long patientId,
            List<PatientDocumentUploadRequest> requests,
            MultipartFile[] files) {

        log.info("Starting batch document upload for patient with id: {}",
                patientId);

        // find patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId
                        )
                );

        // check documents and files
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException(
                    "Document details are required"
            );
        }

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException(
                    "At least one file is required"
            );
        }

        // check request and file count
        if (requests.size() != files.length) {
            throw new IllegalArgumentException(
                    "Number of documents and files must be the same"
            );
        }

        List<PatientDocumentResponse> responses = new ArrayList<>();

        // process all documents in the batch
        for (int i = 0; i < files.length; i++) {

            MultipartFile file = files[i];
            PatientDocumentUploadRequest request = requests.get(i);

            // check file
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException(
                        "File cannot be empty at position: " + i
                );
            }

            // check document type
            if (request.getDocumentType() == null) {
                throw new IllegalArgumentException(
                        "Document type is required at position: " + i
                );
            }

            String filePath = fileStorageService.saveFile(patientId, file);

            PatientDocument document = PatientDocument.builder()
                    .documentType(request.getDocumentType())
                    .documentNumber(request.getDocumentNumber())
                    .fileName(file.getOriginalFilename())
                    .filePath(filePath)
                    .verificationStatus(VerificationStatus.PENDING)
                    .uploadedAt(LocalDateTime.now())
                    .patient(patient)
                    .build();

            // save document
            PatientDocument savedDocument =
                    patientDocumentRepository.save(document);

            responses.add(mapToResponse(savedDocument));

            log.info(
                    "Document processed successfully: {}",
                    filePath
            );
        }

        log.info(
                "Batch document upload completed for patient id: {}. Total documents: {}",
                patientId,
                responses.size()
        );

        return responses;
    }

    @Override
    public PatientDocumentResponse getDocumentById(Long documentId) {

        log.info("Fetching patient document with id: {}", documentId);

        // find document
        PatientDocument document =
                patientDocumentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient document not found with id: "
                                                + documentId
                                )
                        );

        return mapToResponse(document);
    }

    @Override
    public List<PatientDocumentResponse> getDocumentsByPatientId(
            Long patientId) {

        log.info(
                "Fetching documents for patient with id: {}",
                patientId
        );

        // check patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId
            );
        }

        // get all documents of patient
        return patientDocumentRepository
                .findByPatientPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PatientDocumentResponse updateDocument(
            Long documentId,
            PatientDocumentRequest request) {

        log.info(
                "Updating patient document with id: {}",
                documentId
        );

        // find existing document
        PatientDocument document =
                patientDocumentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient document not found with id: "
                                                + documentId
                                )
                        );

        // update document details
        document.setDocumentType(request.getDocumentType());
        document.setDocumentNumber(request.getDocumentNumber());

        // save updated document
        PatientDocument updatedDocument =
                patientDocumentRepository.save(document);

        log.info(
                "Patient document updated successfully with id: {}",
                documentId
        );

        return mapToResponse(updatedDocument);
    }

    @Override
    public void deleteDocument(Long documentId) {

        log.info(
                "Deleting patient document with id: {}",
                documentId
        );

        // check document exists
        if (!patientDocumentRepository.existsById(documentId)) {
            throw new ResourceNotFoundException(
                    "Patient document not found with id: " + documentId
            );
        }

        // delete document
        patientDocumentRepository.deleteById(documentId);

        log.info(
                "Patient document deleted successfully with id: {}",
                documentId
        );
    }

    // convert entity to response
    private PatientDocumentResponse mapToResponse(
            PatientDocument document) {

        return PatientDocumentResponse.builder()
                .documentId(document.getDocumentId())
                .patientId(document.getPatient().getPatientId())
                .documentType(document.getDocumentType())
                .documentNumber(document.getDocumentNumber())
                .fileName(document.getFileName())
                .filePath(document.getFilePath())
                .verificationStatus(document.getVerificationStatus())
                .uploadedAt(document.getUploadedAt())
                .build();
    }
}