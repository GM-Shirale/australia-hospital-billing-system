package com.hospital.hospital_billing_system.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MbsSuggestionRequest {
    @NotBlank(message = "Clinical summary cannot be blank")
    private String clinicalSummary;

    private String doctorSpecialty;
    private Integer consultationDurationMinutes;
}