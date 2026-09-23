package com.hospital.hospital_billing_system.insurance.dto;

import com.hospital.hospital_billing_system.insurance.enums.InsuranceType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequest {

    @NotNull(message = "Patient ID is mandatory")
    private UUID patientId;

    @NotBlank(message = "Policy number is mandatory")
    private String policyNumber;

    @NotBlank(message = "Provider name is mandatory")
    private String providerName; // e.g. Medicare, Bupa, Medibank

    @NotNull(message = "Insurance type is mandatory")
    private InsuranceType insuranceType;

    @NotNull(message = "Coverage limit is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Coverage limit must be greater than zero")
    private BigDecimal coverageLimit;

    @NotNull(message = "Valid from date is mandatory")
    private LocalDate validFrom;

    @NotNull(message = "Valid to date is mandatory")
    private LocalDate validTo;
}