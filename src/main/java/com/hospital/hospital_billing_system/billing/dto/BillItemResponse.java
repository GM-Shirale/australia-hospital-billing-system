package com.hospital.hospital_billing_system.billing.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemResponse {

    // bill item id
    private Long billItemId;

    // bill id
    private Long billId;

    // type of hospital service
    private String serviceType;

    // description of the service
    private String description;

    // number of services
    private Integer quantity;

    // price of one service
    private BigDecimal unitPrice;

    // total amount for this item
    private BigDecimal amount;
}