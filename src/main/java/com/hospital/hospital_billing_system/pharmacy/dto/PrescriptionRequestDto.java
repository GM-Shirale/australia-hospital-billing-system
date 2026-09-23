package com.hospital.hospital_billing_system.pharmacy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequestDto {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    private LocalDate prescriptionDate;

    @Size(
            max = 1000,
            message = "Notes must not exceed 1000 characters"
    )
    private String notes;

}
