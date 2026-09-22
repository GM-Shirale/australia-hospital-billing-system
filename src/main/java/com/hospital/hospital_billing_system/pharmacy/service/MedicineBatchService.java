package com.hospital.hospital_billing_system.pharmacy.service;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineBatchRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineBatchResponseDto;

import java.util.List;

public interface MedicineBatchService {

    MedicineBatchResponseDto createMedicineBatch(
            MedicineBatchRequestDto request
    );

    MedicineBatchResponseDto getMedicineBatchById(
            Long batchId
    );

    List<MedicineBatchResponseDto> getAllMedicineBatches();

    List<MedicineBatchResponseDto> getBatchesByMedicineId(
            Long medicineId
    );

    MedicineBatchResponseDto updateMedicineBatch(
            Long batchId,
            MedicineBatchRequestDto request
    );
}
