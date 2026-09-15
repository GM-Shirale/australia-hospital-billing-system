package com.hospital.hospital_billing_system.pharmacy.dto;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyChargeResponseDto {

    private Long pharmacyChargeId;

    private String chargeNumber;

    private Long dispensingId;

    private Long patientId;

    private Long medicineId;

    private String medicineCode;

    private String genericName;

    private String brandName;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalAmount;

    private BillingType billingType;

    private LabChargeStatus status;

    private LocalDateTime chargedAt;

    private String notes;
}