package com.hospital.hospital_billing_system.billing.dto;

import com.hospital.hospital_billing_system.billing.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long paymentId;

    private String paymentNumber;

    private Long billId;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal remainingAmount;

    private PaymentStatus paymentStatus;

    private LocalDateTime paymentDate;
}