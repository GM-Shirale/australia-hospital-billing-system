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
public class LabResultValueResponseDTO {

    private Long id;

    private Long labResultId;
    private String resultNumber;

    private Long labParameterId;
    private String parameterCode;
    private String parameterName;

    private String resultValue;
    private String unit;
    private String referenceRange;
    private Boolean abnormal;
    private String comments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}