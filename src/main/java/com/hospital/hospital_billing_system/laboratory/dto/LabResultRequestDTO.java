package com.hospital.hospital_billing_system.laboratory.dto;

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
public class LabResultRequestDTO {
    @NotNull(message = " Lab sample ID is required")
    private Long labSampleId;

    @NotNull(message = "Lab test ID is required")
    private Long labTestId;

    @Size(max = 500,message = "Comments must not exceed 500 characters")
    private String comments;

}

