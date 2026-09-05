package com.hospital.hospital_billing_system.billing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bill_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItem {

    // primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_item_id")
    private Long billItemId;

    // bill to which this item belongs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    // type of hospital service
    @Column(name = "service_type", nullable = false, length = 50)
    private String serviceType;

    // description of the service
    @Column(name = "description", length = 255)
    private String description;

    // number of services
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // price of one service
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    // total amount for this item
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
}