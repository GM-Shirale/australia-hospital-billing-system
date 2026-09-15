package com.hospital.hospital_billing_system.pharmacy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispensingRequestDto {

    @NotNull(message = "Prescription item ID is required")
    @Positive(message = "Prescription item ID must be positive")
    private Long prescriptionItemId;

    @NotNull(message = "Stock ID is required")
    @Positive(message = "Stock ID must be positive")
    private Long stockId;

    @NotNull(message = "Quantity dispensed is required")
    @Positive(message = "Quantity dispensed must be greater than zero")
    private Integer quantityDispensed;

    @NotBlank(message = "Dispensed by is required")
    @Size(max = 100, message = "Dispensed by must not exceed 100 characters")
    private String dispensedBy;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}