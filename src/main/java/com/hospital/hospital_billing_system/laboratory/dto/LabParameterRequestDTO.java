package com.hospital.hospital_billing_system.laboratory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabParameterRequestDTO {

    @NotBlank(message = "Parameter code is required")
    @Size(max = 50,message = "Parameter code must not exceed 50 char")
    private String parameterCode;

    @NotBlank(message = "Result type is required")
    @Size(max = 50,message = "Result type must not exceed 50 characters")
    private  String resultType;

    @NotBlank(message = "Parameter name is required")
    @Size(max = 100,message = "parameter name must not exceed 100 char")
    private String parameterName;

    @Size(max = 300,message = "Unit must not exceed 30 char")
    private String unit;

    @Size(max = 100,message = "Reference range must not exceed 100 characters")
    private String referenceRange;

    @Size(max = 500,message ="Description must not exceed 500 characters" )
    private String description;

    @NotNull(message = "Active status is required")
    private Boolean active;

    @NotNull(message = "Lb test ID is required")
    private Long labTestId;

}
