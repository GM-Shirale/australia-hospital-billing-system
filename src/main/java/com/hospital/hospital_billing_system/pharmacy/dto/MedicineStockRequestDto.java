package com.hospital.hospital_billing_system.pharmacy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineStockRequestDto {
    @NotNull(message = "Medicine ID is required")
    @Positive(message = "Medicine ID must be positive")
    private Long medicineId;

    @NotNull(message = "Batch ID is required")
    @Positive(message = "Batch ID must be positive")
    private Long batchId;

    @NotNull(message = "Quantity received is required")
    @Positive(message = "Quantity received must be greater than zero")
    private Integer quantityReceived;
}
