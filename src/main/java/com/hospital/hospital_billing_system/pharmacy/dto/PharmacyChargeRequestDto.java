package com.hospital.hospital_billing_system.pharmacy.dto;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyChargeRequestDto {

    @NotNull(message = "Dispensing ID is required")
    @Positive(message = "Dispensing ID must be positive")
    private Long dispensingId;

    @NotNull(message = "Billing type is required")
    private BillingType billingType;

    @Size(
            max = 500,
            message = "Notes must not exceed 500 characters"
    )
    private String notes;
}