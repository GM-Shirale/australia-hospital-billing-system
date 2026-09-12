package com.hospital.hospital_billing_system.billing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false, unique = true)
    private Bill bill;

    @Column(name = "invoice_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal invoiceAmount;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_status", nullable = false, length = 30)
    private InvoiceStatus invoiceStatus;
}