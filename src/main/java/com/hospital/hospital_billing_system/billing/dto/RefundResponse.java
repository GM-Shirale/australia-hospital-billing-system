package com.hospital.hospital_billing_system.billing.dto;

import com.hospital.hospital_billing_system.billing.entity.RefundStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundResponse {

    private Long refundId;
    private String refundNumber;
    private Long transactionId;
    private BigDecimal refundAmount;
    private RefundStatus refundStatus;
    private String refundReason;
    private LocalDateTime refundDate;
}