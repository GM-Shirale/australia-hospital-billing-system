package com.hospital.hospital_billing_system.pharmacy.dto;

import lombok.*;

import java.time.LocalDateTime;

   @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class PrescriptionItemResponseDto {

        private Long prescriptionItemId;

        private Long prescriptionId;

        private Long medicineId;

        private String medicineCode;

        private String genericName;

        private String brandName;

        private String strength;

        private String dosage;

        private String frequency;

        private Integer duration;

        private String durationUnit;

        private Integer quantity;

        private Integer dispensedQuantity;

        private Integer remainingQuantity;

        private String instructions;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }


