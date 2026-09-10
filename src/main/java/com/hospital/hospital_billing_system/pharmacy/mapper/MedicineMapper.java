package com.hospital.hospital_billing_system.pharmacy.mapper;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.MedicineResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {

    public Medicine toEntity(MedicineRequestDto request) {

        return Medicine.builder()
                .medicineCode(request.getMedicineCode())
                .genericName(request.getGenericName())
                .brandName(request.getBrandName())
                .strength(request.getStrength())
                .dosageForm(request.getDosageForm())
                .route(request.getRoute())
                .manufacturer(request.getManufacturer())
                .pbsItemCode(request.getPbsItemCode())
                .prescriptionRequired(request.getPrescriptionRequired())
                .unitPrice(request.getUnitPrice())
                .reorderLevel(request.getReorderLevel())
                .active(request.getActive())
                .build();
    }

    public MedicineResponseDto toResponseDto(Medicine medicine) {

        return MedicineResponseDto.builder()
                .medicineId(medicine.getMedicineId())
                .medicineCode(medicine.getMedicineCode())
                .genericName(medicine.getGenericName())
                .brandName(medicine.getBrandName())
                .strength(medicine.getStrength())
                .dosageForm(medicine.getDosageForm())
                .route(medicine.getRoute())
                .manufacturer(medicine.getManufacturer())
                .pbsItemCode(medicine.getPbsItemCode())
                .prescriptionRequired(medicine.getPrescriptionRequired())
                .unitPrice(medicine.getUnitPrice())
                .reorderLevel(medicine.getReorderLevel())
                .active(medicine.getActive())
                .createdAt(medicine.getCreatedAt())
                .updatedAt(medicine.getUpdatedAt())
                .build();
    }
}