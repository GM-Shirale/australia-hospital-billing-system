package com.hospital.hospital_billing_system.billing.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponse {

    // bill id
    private Long billId;

    // hospital bill number
    private String billNumber;

    // patient id
    private Long patientId;

    // total hospital bill amount
    private BigDecimal totalAmount;

    // amount covered by insurance
    private BigDecimal insuranceAmount;

    // amount to be paid by patient
    private BigDecimal patientAmount;

    // bill generation date
    private LocalDateTime billDate;
}