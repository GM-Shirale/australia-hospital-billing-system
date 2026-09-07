package com.hospital.hospital_billing_system.laboratory.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabResultValueRequestDTO {

    @NotNull(message = "Lab result ID is required")
    private Long labResultId;

    @NotNull(message = "Lab parameter ID is required")
    private Long labParameterId;

    @NotBlank(message = "Result value is required")
    @Size(max = 255, message = "Result value must not exceed 255 characters")
    private String resultValue;

    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit;

    @Size(max = 100, message = "Reference range must not exceed 100 characters")
    private String referenceRange;

    private Boolean abnormal;

    @Size(max = 500, message = "Comments must not exceed 500 characters")
    private String comments;
}