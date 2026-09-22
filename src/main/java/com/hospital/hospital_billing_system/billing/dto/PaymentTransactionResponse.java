package com.hospital.hospital_billing_system.billing.dto;

import com.hospital.hospital_billing_system.billing.entity.PaymentMethod;
import com.hospital.hospital_billing_system.billing.entity.PaymentTransactionStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransactionResponse {

    private Long transactionId;
    private String transactionNumber;
    private Long paymentId;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private PaymentTransactionStatus transactionStatus;
    private String gatewayName;
    private String gatewayTransactionId;
    private LocalDateTime transactionDate;
    private String failureReason;
}