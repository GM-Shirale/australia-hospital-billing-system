package com.hospital.hospital_billing_system.laboratory.dto;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabChargeRequestDTO {

    @NotNull(message = "Lab order item ID is required")
    private Long labOrderItemId;

    @NotNull(message = "Provider charge is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Provider charge must not be negative")
    @Digits(integer = 10, fraction = 2, message = "Provider charge must have maximum 10 integer digits and 2 decimal places")
    private BigDecimal providerCharge;

    @Size(max = 20, message = "MBS item number must not exceed 20 characters")
    private String mbsItemNumber;

    @DecimalMin(value = "0.00", inclusive = true, message = "Medicare benefit must not be negative")
    @Digits(integer = 10, fraction = 2, message = "Medicare benefit must have maximum 10 integer digits and 2 decimal places")
    private BigDecimal medicareBenefit;

    @NotNull(message = "Patient amount is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Patient amount must not be negative")
    @Digits(integer = 10, fraction = 2, message = "Patient amount must have maximum 10 integer digits and 2 decimal places")
    private BigDecimal patientAmount;

    @NotNull(message = "Billing type is required")
    private BillingType billingType;

    @NotNull(message = "Charge status is required")
    private LabChargeStatus status;
}