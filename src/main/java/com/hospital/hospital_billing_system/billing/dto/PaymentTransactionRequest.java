package com.hospital.hospital_billing_system.billing.dto;

import com.hospital.hospital_billing_system.billing.entity.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransactionRequest {

    private Long paymentId;

    private PaymentMethod paymentMethod;

    private BigDecimal amount;

    private String gatewayName;

    private String gatewayTransactionId;
}