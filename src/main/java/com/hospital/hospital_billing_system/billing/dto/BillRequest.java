package com.hospital.hospital_billing_system.billing.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillRequest {

    // total amount of the hospital bill
    private BigDecimal totalAmount;

    // amount covered by insurance
    private BigDecimal insuranceAmount;

    // amount to be paid by patient
    private BigDecimal patientAmount;
}