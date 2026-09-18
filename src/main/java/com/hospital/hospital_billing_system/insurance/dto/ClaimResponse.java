package com.hospital.hospital_billing_system.insurance.dto;

import com.hospital.hospital_billing_system.insurance.enums.ClaimStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimResponse {

    private UUID claimId;
    private UUID billId;
    private UUID patientId;
    private UUID policyId;
    private String policyNumber;
    private String providerName;
    private BigDecimal claimedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal patientCoPayment;
    private ClaimStatus status;
    private String rejectionReason;
    private LocalDateTime claimDate;
    private LocalDateTime settlementDate;
}