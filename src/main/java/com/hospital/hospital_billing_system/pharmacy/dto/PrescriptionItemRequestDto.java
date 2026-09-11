package com.hospital.hospital_billing_system.pharmacy.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItemRequestDto {

    @NotNull(message = "Prescription ID is require")
    @Positive(message = "Prescription ID must be positive")
    private Long prescriptionId;

    @NotNull(message = "Medicine ID is required")
    @Positive(message = "Medicine ID must be positive")
    private Long medicineId;

    @NotBlank(message = "Dosage is required")
    @Size(max = 100, message = "Dosage must not exceed 100 characters")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Size(max = 100, message = "Frequency must not exceed 100 characters")
    private String frequency;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be greater than zero")
    private Integer duration;

    @NotBlank(message = "Duration unit is required")
    @Size(max = 20, message = "Duration unit must not exceed 20 characters")
    private String durationUnit;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @Size(max = 500, message = "Instructions must not exceed 500 characters")
    private String instructions;

}
