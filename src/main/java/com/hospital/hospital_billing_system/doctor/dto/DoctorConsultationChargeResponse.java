package com.hospital.hospital_billing_system.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Outbound response carrying verified practitioner information and billing eligibility
 * ready to be consumed as a BillItem by Central Billing.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorConsultationChargeResponse {

    private UUID doctorId;
    private UUID tenantId;
    private String doctorFullName;
    private String specialization;
    private String providerNo;
    private UUID departmentId;
    private String departmentName;
    private BigDecimal consultationFee;
    private boolean mbsBillable;
    private Instant verifiedAt;
}