package com.hospital.hospital_billing_system.pharmacy.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispensingResponseDto {

    private Long dispensingId;

    private Long prescriptionItemId;
    private Long prescriptionId;

    private Long medicineId;
    private String medicineCode;
    private String genericName;

    private Long stockId;
    private Long batchId;
    private String batchNumber;

    private Integer quantityDispensed;

    private Integer stockQuantityAvailable;
    private Integer prescriptionRemainingQuantity;

    private LocalDateTime dispensedAt;

    private String dispensedBy;

    private String notes;
}