package com.hospital.hospital_billing_system.pharmacy.mapper;

import com.hospital.hospital_billing_system.pharmacy.dto.DispensingResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Dispensing;
import org.springframework.stereotype.Component;

@Component
public class DispensingMapper {

    public DispensingResponseDto toResponseDto(Dispensing dispensing) {

        if (dispensing == null) {
            return null;
        }

        return DispensingResponseDto.builder()
                .dispensingId(dispensing.getDispensingId())

                .prescriptionItemId(
                        dispensing.getPrescriptionItem().getPrescriptionItemId()
                )

                .prescriptionId(
                        dispensing.getPrescriptionItem()
                                .getPrescription()
                                .getPrescriptionId()
                )

                .medicineId(
                        dispensing.getPrescriptionItem()
                                .getMedicine()
                                .getMedicineId()
                )

                .medicineCode(
                        dispensing.getPrescriptionItem()
                                .getMedicine()
                                .getMedicineCode()
                )

                .genericName(
                        dispensing.getPrescriptionItem()
                                .getMedicine()
                                .getGenericName()
                )

                .stockId(
                        dispensing.getStock().getStockId()
                )

                .batchId(
                        dispensing.getStock()
                                .getBatch()
                                .getBatchId()
                )

                .batchNumber(
                        dispensing.getStock()
                                .getBatch()
                                .getBatchNumber()
                )

                .quantityDispensed(
                        dispensing.getQuantityDispensed()
                )

                .stockQuantityAvailable(
                        dispensing.getStock()
                                .getQuantityAvailable()
                )

                .prescriptionRemainingQuantity(
                        dispensing.getPrescriptionItem()
                                .getRemainingQuantity()
                )

                .dispensedAt(
                        dispensing.getDispensedAt()
                )

                .dispensedBy(
                        dispensing.getDispensedBy()
                )

                .notes(
                        dispensing.getNotes()
                )

                .build();
    }
}