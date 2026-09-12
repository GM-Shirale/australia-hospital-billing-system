package com.hospital.hospital_billing_system.billing.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillRequest {

    private Long patientId;

    private BigDecimal totalAmount;

    private BigDecimal patientAmount;

    private BigDecimal insuranceAmount;

    private BigDecimal medicareAmount;
}