package com.hospital.hospital_billing_system.insurance.dto;

import com.hospital.hospital_billing_system.insurance.enums.InsuranceType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyResponse {

    private UUID id;
    private UUID tenantId;
    private UUID patientId;
    private String policyNumber;
    private String providerName;
    private InsuranceType insuranceType;
    private BigDecimal coverageLimit;
    private BigDecimal remainingLimit;
    private LocalDate validFrom;
    private LocalDate validTo;
    private boolean active;
}