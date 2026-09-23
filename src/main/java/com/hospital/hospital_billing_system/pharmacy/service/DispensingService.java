package com.hospital.hospital_billing_system.pharmacy.service;

import com.hospital.hospital_billing_system.pharmacy.dto.DispensingRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.DispensingResponseDto;

import java.util.List;

public interface DispensingService {

    DispensingResponseDto createDispensing(
            DispensingRequestDto requestDto
    );

    DispensingResponseDto getDispensingById(
            Long dispensingId
    );

    List<DispensingResponseDto> getDispensingByPrescriptionItemId(
            Long prescriptionItemId
    );

    List<DispensingResponseDto> getDispensingByStockId(
            Long stockId
    );

    List<DispensingResponseDto> getDispensingByPrescriptionId(
            Long prescriptionId
    );
}