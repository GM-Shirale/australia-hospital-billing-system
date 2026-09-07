package com.hospital.hospital_billing_system.doctor.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Inbound request payload used by Admissions and Billing modules
 * to validate a doctor and generate a clinical consultation charge.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorConsultationChargeRequest {

    @NotNull(message = "Doctor ID is mandatory")
    private UUID doctorId;

    @NotNull(message = "Tenant ID is mandatory")
    private UUID tenantId;

    /**
     * MBS Item code or Consultation fee rate in AUD.
     */
    @NotNull(message = "Consultation fee is mandatory")
    @DecimalMin(value = "0.00", message = "Consultation fee cannot be negative")
    private BigDecimal consultationFee;
}