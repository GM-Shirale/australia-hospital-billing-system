package com.hospital.hospital_billing_system.doctor.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Inbound payload representing doctor consultation charge generated for central billing.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorConsultationChargeRequest {

    @NotNull(message = "Tenant ID is mandatory")
    private UUID tenantId;

    @NotNull(message = "Patient ID is mandatory")
    private UUID patientId;

    @NotNull(message = "Doctor ID is mandatory")
    private UUID doctorId;

    // Australian MBS (Medicare Benefits Schedule) Item code (e.g., "23" for standard GP consultation)
    @NotBlank(message = "MBS Item Code is required for billing")
    private String mbsItemCode;

    @NotNull(message = "Fee amount is required")
    @DecimalMin(value = "0.00", message = "Consultation fee cannot be negative")
    private BigDecimal feeAmount;
}