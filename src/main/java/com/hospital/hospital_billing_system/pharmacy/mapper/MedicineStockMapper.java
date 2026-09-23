package com.hospital.hospital_billing_system.pharmacy.mapper;

import com.hospital.hospital_billing_system.pharmacy.dto.MedicineStockResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.MedicineStock;
import org.springframework.stereotype.Component;

@Component
public class MedicineStockMapper {

    public MedicineStockResponseDto toResponseDto(
            MedicineStock stock) {

        if (stock == null) {
            return null;
        }

        return MedicineStockResponseDto.builder()
                .stockId(stock.getStockId())

                .medicineId(
                        stock.getMedicine().getMedicineId()
                )
                .medicineCode(
                        stock.getMedicine().getMedicineCode()
                )
                .genericName(
                        stock.getMedicine().getGenericName()
                )
                .brandName(
                        stock.getMedicine().getBrandName()
                )
                .strength(
                        stock.getMedicine().getStrength()
                )

                .batchId(
                        stock.getBatch().getBatchId()
                )
                .batchNumber(
                        stock.getBatch().getBatchNumber()
                )
                .expiryDate(
                        stock.getBatch().getExpiryDate()
                )

                .quantityReceived(
                        stock.getQuantityReceived()
                )
                .quantityAvailable(
                        stock.getQuantityAvailable()
                )
                .quantityDispensed(
                        stock.getQuantityDispensed()
                )
                .active(
                        stock.getActive()
                )
                .createdAt(
                        stock.getCreatedAt()
                )
                .updatedAt(
                        stock.getUpdatedAt()
                )
                .build();
}
}
