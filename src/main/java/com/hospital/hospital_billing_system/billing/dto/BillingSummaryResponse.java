package com.hospital.hospital_billing_system.billing.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingSummaryResponse {

    private Long totalBills;

    private BigDecimal totalBilledAmount;

    private BigDecimal totalPatientAmount;

    private BigDecimal totalPaidAmount;

    private BigDecimal totalRemainingAmount;

    private BigDecimal totalRefundedAmount;
}