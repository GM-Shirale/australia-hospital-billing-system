package com.hospital.hospital_billing_system.billing.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemResponse {

    private Long billItemId;
    private Long billId;
    private String itemName;
    private String itemType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;

}