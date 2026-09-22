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
public class LabResultResponseDTO {

    private Long id;

    private String resultNumber;

    private Long labSampleId;
    private String sampleNumber;

    private Long labOrderId;
    private String orderNumber;

    private Long labTestId;
    private String testCode;
    private String testName;

    private String status;

    private LocalDateTime resultDate;

    private String comments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
