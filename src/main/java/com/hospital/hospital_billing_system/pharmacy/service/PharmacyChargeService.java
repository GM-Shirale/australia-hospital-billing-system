package com.hospital.hospital_billing_system.pharmacy.service;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import com.hospital.hospital_billing_system.pharmacy.dto.PharmacyChargeRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PharmacyChargeResponseDto;

import java.util.List;

public interface PharmacyChargeService {

    PharmacyChargeResponseDto createPharmacyCharge(
            PharmacyChargeRequestDto requestDto
    );

    PharmacyChargeResponseDto getPharmacyChargeById(
            Long pharmacyChargeId
    );

    PharmacyChargeResponseDto getPharmacyChargeByChargeNumber(
            String chargeNumber
    );

    PharmacyChargeResponseDto getPharmacyChargeByDispensingId(
            Long dispensingId
    );

    List<PharmacyChargeResponseDto> getPharmacyChargesByPatientId(
            Long patientId
    );

    List<PharmacyChargeResponseDto> getPharmacyChargesByMedicineId(
            Long medicineId
    );

    List<PharmacyChargeResponseDto> getPharmacyChargesByStatus(
            LabChargeStatus status
    );

    List<PharmacyChargeResponseDto> getPharmacyChargesByBillingType(
            BillingType billingType
    );
}