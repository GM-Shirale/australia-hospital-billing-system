package com.hospital.hospital_billing_system.laboratory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabParameterResponseDTO {

    private Long id;

    private String parameterName;

    private String unit;

    private String referenceRange;

    private String description;

    private Boolean active;

    private Long labTestId;

    private String labTestName;

    private String parameterCode;

    private String resultType;

}
