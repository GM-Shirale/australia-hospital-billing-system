package com.hospital.hospital_billing_system.pharmacy.service;

import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionItemRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionItemResponseDto;

import java.util.List;

public interface PrescriptionItemService {

    PrescriptionItemResponseDto createPrescriptionItem(
            PrescriptionItemRequestDto requestDto
    );

    PrescriptionItemResponseDto getPrescriptionItemById(
            Long prescriptionItemId
    );

    List<PrescriptionItemResponseDto> getItemsByPrescriptionId(
            Long prescriptionId
    );

    List<PrescriptionItemResponseDto> getItemsByMedicineId(
            Long medicineId
    );

    PrescriptionItemResponseDto updatePrescriptionItem(
            Long prescriptionItemId,
            PrescriptionItemRequestDto requestDto
    );

    void deletePrescriptionItem(
            Long prescriptionItemId
    );

}
