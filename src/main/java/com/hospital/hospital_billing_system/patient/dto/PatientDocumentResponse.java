package com.hospital.hospital_billing_system.patient.dto;


import com.hospital.hospital_billing_system.common.enums.DocumentType;
import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDocumentResponse {

    // document id
    private Long documentId;

    // patient id
    private Long patientId;

    // type of document
    private DocumentType documentType;

    // document number
    private String documentNumber;

    // uploaded file name
    private String fileName;

    // location where file is stored
    private String filePath;

    // document verification status
    private VerificationStatus verificationStatus;

    // document upload time
    private LocalDateTime uploadedAt;
}
