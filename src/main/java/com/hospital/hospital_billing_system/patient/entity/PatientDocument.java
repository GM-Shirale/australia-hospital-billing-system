package com.hospital.hospital_billing_system.patient.entity;


import com.hospital.hospital_billing_system.common.enums.DocumentType;
import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDocument {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    // type of patient document
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 50)
    private DocumentType documentType;

    // document number
    @Column(name = "document_number", length = 100)
    private String documentNumber;

    // uploaded file name
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    // location where file is stored
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    // document verification status
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 30)
    private VerificationStatus verificationStatus;

    // document upload time
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    // patient who owns this document
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
}