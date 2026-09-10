package com.hospital.hospital_billing_system.pharmacy.dto;

import com.hospital.hospital_billing_system.common.enums.MedicineBatchStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MedicineBatchResponseDto {

    private Long batchId;

    private Long medicineId;

    private String batchNumber;

    private LocalDate manufacturingDate;

    private LocalDate expiryDate;

    private Integer receivedQuantity;

    private Integer quantity;

    private BigDecimal unitCost;

    private String supplierName;

    private MedicineBatchStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
