package com.hospital.hospital_billing_system.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineStockResponseDto {
    private Long stockId;

    private Long medicineId;
    private String medicineCode;
    private String genericName;
    private String brandName;
    private String strength;

    private Long batchId;
    private String batchNumber;
    private LocalDate expiryDate;

    private Integer quantityReceived;
    private Integer quantityAvailable;
    private Integer quantityDispensed;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
