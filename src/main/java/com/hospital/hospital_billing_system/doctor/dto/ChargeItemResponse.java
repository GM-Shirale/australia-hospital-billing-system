package com.hospital.hospital_billing_system.doctor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Common outbound charge representation forwarded to Central Billing engine (BillItem).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeItemResponse {

    private String itemType; // "DOCTOR_CONSULTATION" or "ROOM_ACCOMMODATION"
    private UUID referenceId; // Doctor ID or Room ID
    private String description;
    private String providerNumber; // Mandatory for Medicare claim
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;
}