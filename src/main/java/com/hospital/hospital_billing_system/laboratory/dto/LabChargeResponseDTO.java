package com.hospital.hospital_billing_system.laboratory.dto;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabChargeResponseDTO {

    private Long id;

    private String chargeNumber;

    private Long labOrderItemId;

    private Long labOrderId;

    private String orderNumber;

    private Long labTestId;

    private String testCode;

    private String testName;

    private BigDecimal providerCharge;

    private String mbsItemNumber;

    private BigDecimal medicareBenefit;

    private BigDecimal patientAmount;

    private BillingType billingType;

    private LabChargeStatus status;

    private LocalDateTime chargedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}