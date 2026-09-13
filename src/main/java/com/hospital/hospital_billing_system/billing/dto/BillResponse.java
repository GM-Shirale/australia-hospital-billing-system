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

    private Long billId;

    private String billNumber;

    private Long patientId;

    private BigDecimal totalAmount;

    private BigDecimal patientAmount;

    private BigDecimal insuranceAmount;

    private BigDecimal medicareAmount;

    private LocalDateTime billDate;
}