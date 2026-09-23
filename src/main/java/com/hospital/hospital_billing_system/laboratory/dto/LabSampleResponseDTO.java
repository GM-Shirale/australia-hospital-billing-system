package com.hospital.hospital_billing_system.laboratory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabSampleResponseDTO {
    private Long id;

    private String sampleNumber;

    private Long labOrderId;

    private String orderNumber;

    private String sampleType;

    private String barcode;

    private LocalDateTime collectedAt;

    private LocalDateTime receivedAt;

    private String status;

    private String collectionNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
