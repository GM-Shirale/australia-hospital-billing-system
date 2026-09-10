package com.hospital.hospital_billing_system.pharmacy.mapper;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineBatchRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineBatchResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Medicine;
import com.hospital.hospital_billing_system.pharmacy.entity.MedicineBatch;
import org.springframework.stereotype.Component;

@Component
public class MedicineBatchMapper {
    public MedicineBatch toEntity(
            MedicineBatchRequestDto request,
            Medicine medicine) {

        return MedicineBatch.builder()
                .medicine(medicine)
                .batchNumber(request.getBatchNumber())
                .manufacturingDate(request.getManufacturingDate())
                .expiryDate(request.getExpiryDate())
                .receivedQuantity(request.getReceivedQuantity())
                .quantity(request.getQuantity())
                .unitCost(request.getUnitCost())
                .supplierName(request.getSupplierName())
                .build();
    }

    public MedicineBatchResponseDto toResponseDto(
            MedicineBatch medicineBatch) {

        return MedicineBatchResponseDto.builder()
                .batchId(medicineBatch.getBatchId())
                .medicineId(medicineBatch.getMedicine().getMedicineId())
                .batchNumber(medicineBatch.getBatchNumber())
                .manufacturingDate(medicineBatch.getManufacturingDate())
                .expiryDate(medicineBatch.getExpiryDate())
                .receivedQuantity(medicineBatch.getReceivedQuantity())
                .quantity(medicineBatch.getQuantity())
                .unitCost(medicineBatch.getUnitCost())
                .supplierName(medicineBatch.getSupplierName())
                .status(medicineBatch.getStatus())
                .createdAt(medicineBatch.getCreatedAt())
                .updatedAt(medicineBatch.getUpdatedAt())
                .build();
    }
}
