package com.hospital.hospital_billing_system.laboratory.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabOrderItemResponseDTO {

    private Long id;

    private Long labOrderId;
    private String orderNumber;

    private Long labTestId;
    private String testCode;
    private String testName;

    private Integer quantity;
    private BigDecimal price;
    private String status;
    private String clinicalNotes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}