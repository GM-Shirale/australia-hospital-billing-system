package com.hospital.hospital_billing_system.laboratory.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabTestResponseDTO {

    private Long id;

    private String testCode;

    private String testName;

    private String category;

    private String sampleType;

    private String description;

    private BigDecimal price;

    private Integer turnaroundTime;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
