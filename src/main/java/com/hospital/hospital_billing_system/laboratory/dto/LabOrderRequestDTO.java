package com.hospital.hospital_billing_system.laboratory.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabOrderRequestDTO {

    @NotBlank(message = "Order number is required")
    @Size(max = 50, message = "Order number must not exceed 50 characters")
    private String orderNumber;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @Size(max = 500, message = "Clinical notes must not exceed 500 characters")
    private String clinicalNotes;
}
