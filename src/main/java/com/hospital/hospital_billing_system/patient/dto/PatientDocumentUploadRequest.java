package com.hospital.hospital_billing_system.patient.dto;


import com.hospital.hospital_billing_system.common.enums.DocumentType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDocumentUploadRequest {

    // type of document
    private DocumentType documentType;

    // document number
    private String documentNumber;
}
