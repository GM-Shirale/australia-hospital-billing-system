package com.hospital.hospital_billing_system.pharmacy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineStatusRequestDto {

    @NotNull(message = "Active status is required")
    private Boolean active;
}
