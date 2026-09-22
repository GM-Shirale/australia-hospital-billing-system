package com.hospital.hospital_billing_system.billing.dto;

import com.hospital.hospital_billing_system.billing.entity.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {

    private Long invoiceId;
    private String invoiceNumber;
    private Long billId;
    private BigDecimal invoiceAmount;
    private LocalDateTime invoiceDate;
    private InvoiceStatus invoiceStatus;

}