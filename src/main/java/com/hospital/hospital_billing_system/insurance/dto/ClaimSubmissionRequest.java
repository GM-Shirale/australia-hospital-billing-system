package com.hospital.hospital_billing_system.insurance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimSubmissionRequest {

    @NotNull(message = "Bill ID is mandatory")
    private UUID billId;

    @NotNull(message = "Patient ID is mandatory")
    private UUID patientId;

    @NotNull(message = "Policy ID is mandatory")
    private UUID policyId;

    @NotNull(message = "Claimed amount is mandatory")
    @DecimalMin(value = "0.01", message = "Claimed amount must be greater than zero")
    private BigDecimal claimedAmount;

    @NotNull(message = "Service date is mandatory")
    private LocalDate serviceDate;
}