package com.hospital.hospital_billing_system.pharmacy.mapper;


import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionItemResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.PrescriptionItem;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionItemMapper {
    public PrescriptionItemResponseDto toResponseDto(
            PrescriptionItem prescriptionItem) {

        if (prescriptionItem == null) {
            return null;
        }

        return PrescriptionItemResponseDto.builder()
                .prescriptionItemId(
                        prescriptionItem.getPrescriptionItemId()
                )
                .prescriptionId(
                        prescriptionItem.getPrescription()
                                .getPrescriptionId()
                )
                .medicineId(
                        prescriptionItem.getMedicine()
                                .getMedicineId()
                )
                .medicineCode(
                        prescriptionItem.getMedicine()
                                .getMedicineCode()
                )
                .genericName(
                        prescriptionItem.getMedicine()
                                .getGenericName()
                )
                .brandName(
                        prescriptionItem.getMedicine()
                                .getBrandName()
                )
                .strength(
                        prescriptionItem.getMedicine()
                                .getStrength()
                )
                .dosage(
                        prescriptionItem.getDosage()
                )
                .frequency(
                        prescriptionItem.getFrequency()
                )
                .duration(
                        prescriptionItem.getDuration()
                )
                .durationUnit(
                        prescriptionItem.getDurationUnit()
                )
                .quantity(
                        prescriptionItem.getQuantity()
                )
                .dispensedQuantity(
                        prescriptionItem.getDispensedQuantity()
                )
                .remainingQuantity(
                        prescriptionItem.getRemainingQuantity()
                )
                .instructions(
                        prescriptionItem.getInstructions()
                )
                .createdAt(
                        prescriptionItem.getCreatedAt()
                )
                .updatedAt(
                        prescriptionItem.getUpdatedAt()
                )
                .build();

}
}
