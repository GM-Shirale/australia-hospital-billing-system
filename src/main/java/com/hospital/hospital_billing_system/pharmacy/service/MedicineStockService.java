package com.hospital.hospital_billing_system.pharmacy.service;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineStockRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineStockResponseDto;

import java.util.List;

public interface MedicineStockService {

    MedicineStockResponseDto createStock(
            MedicineStockRequestDto requestDto
    );

    MedicineStockResponseDto getStockById(
            Long stockId
    );

    List<MedicineStockResponseDto> getStockByMedicineId(
            Long medicineId
    );

    List<MedicineStockResponseDto> getStockByBatchId(
            Long batchId
    );

    List<MedicineStockResponseDto> getActiveStock();

    List<MedicineStockResponseDto> getLowStock();

    MedicineStockResponseDto updateStock(
            Long stockId,
            MedicineStockRequestDto requestDto
    );

    void deleteStock(
            Long stockId
    );
}
