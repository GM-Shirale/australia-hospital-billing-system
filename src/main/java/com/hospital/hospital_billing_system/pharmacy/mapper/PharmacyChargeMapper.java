package com.hospital.hospital_billing_system.pharmacy.mapper;

import com.hospital.hospital_billing_system.pharmacy.dto.PharmacyChargeResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.PharmacyCharge;
import org.springframework.stereotype.Component;

@Component
public class PharmacyChargeMapper {

    public PharmacyChargeResponseDto toResponseDto(
            PharmacyCharge charge) {

        if (charge == null) {
            return null;
        }

        return PharmacyChargeResponseDto.builder()
                .pharmacyChargeId(
                        charge.getPharmacyChargeId()
                )
                .chargeNumber(
                        charge.getChargeNumber()
                )
                .dispensingId(
                        charge.getDispensing()
                                .getDispensingId()
                )
                .patientId(
                        charge.getPatientId()
                )
                .medicineId(
                        charge.getMedicineId()
                )
                .medicineCode(
                        charge.getDispensing()
                                .getPrescriptionItem()
                                .getMedicine()
                                .getMedicineCode()
                )
                .genericName(
                        charge.getDispensing()
                                .getPrescriptionItem()
                                .getMedicine()
                                .getGenericName()
                )
                .brandName(
                        charge.getDispensing()
                                .getPrescriptionItem()
                                .getMedicine()
                                .getBrandName()
                )
                .quantity(
                        charge.getQuantity()
                )
                .unitPrice(
                        charge.getUnitPrice()
                )
                .totalAmount(
                        charge.getTotalAmount()
                )
                .billingType(
                        charge.getBillingType()
                )
                .status(
                        charge.getStatus()
                )
                .chargedAt(
                        charge.getChargedAt()
                )
                .notes(
                        charge.getNotes()
                )
                .build();
    }
}