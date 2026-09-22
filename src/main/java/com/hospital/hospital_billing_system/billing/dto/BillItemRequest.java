package com.hospital.hospital_billing_system.billing.dto;

import com.hospital.hospital_billing_system.billing.entity.BillItemType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemRequest {

    private Long billId;

    private String itemName;

    private BillItemType itemType;

    private Integer quantity;

    private BigDecimal unitPrice;
}