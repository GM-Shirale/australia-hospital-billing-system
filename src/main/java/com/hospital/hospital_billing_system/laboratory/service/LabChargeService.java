package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import com.hospital.hospital_billing_system.laboratory.dto.LabChargeRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabChargeResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface LabChargeService {

    LabChargeResponseDTO createCharge(
            LabChargeRequestDTO request
    );

    LabChargeResponseDTO getChargeById(
            Long id
    );

    LabChargeResponseDTO getChargeByChargeNumber(
            String chargeNumber
    );

    List<LabChargeResponseDTO> getChargesByLabOrderItemId(
            Long labOrderItemId
    );

    List<LabChargeResponseDTO> getChargesByStatus(
            LabChargeStatus status
    );

    List<LabChargeResponseDTO> getChargesByBillingType(
            BillingType billingType
    );

    // ============================
    // Total Laboratory Cost
    // ============================

    BigDecimal getTotalProviderChargeByLabOrderId(
            Long labOrderId
    );

    BigDecimal getTotalMedicareBenefitByLabOrderId(
            Long labOrderId
    );

    BigDecimal getTotalPatientAmountByLabOrderId(
            Long labOrderId
    );

    LabChargeResponseDTO updateCharge(
            Long id,
            LabChargeRequestDTO request
    );

    void deleteCharge(
            Long id
    );
}