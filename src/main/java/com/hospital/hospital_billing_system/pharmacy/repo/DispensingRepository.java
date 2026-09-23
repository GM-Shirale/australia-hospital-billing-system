package com.hospital.hospital_billing_system.pharmacy.repo;

import com.hospital.hospital_billing_system.pharmacy.entity.Dispensing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispensingRepository extends JpaRepository<Dispensing, Long> {

    List<Dispensing> findByPrescriptionItemPrescriptionItemId(
            Long prescriptionItemId
    );

    List<Dispensing> findByStockStockId(
            Long stockId
    );

    List<Dispensing> findByPrescriptionItemPrescriptionPrescriptionId(
            Long prescriptionId
    );
}