package com.hospital.hospital_billing_system.billing.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemRequest {

    // type of hospital service
    private String serviceType;

    // description of the service
    private String description;

    // number of services
    private Integer quantity;

    // price of one service
    private BigDecimal unitPrice;
}