package com.hospital.hospital_billing_system.pharmacy.repo;

import com.hospital.hospital_billing_system.pharmacy.entity.PharmacyCharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PharmacyChargeRepository
        extends JpaRepository<PharmacyCharge, Long> {

    Optional<PharmacyCharge> findByChargeNumber(
            String chargeNumber
    );

    boolean existsByChargeNumber(
            String chargeNumber
    );

    Optional<PharmacyCharge> findByDispensingDispensingId(
            Long dispensingId
    );

    List<PharmacyCharge> findByPatientId(
            Long patientId
    );

    List<PharmacyCharge> findByMedicineId(
            Long medicineId
    );

    List<PharmacyCharge> findByStatus(
            com.hospital.hospital_billing_system.common.enums.LabChargeStatus status
    );

    List<PharmacyCharge> findByBillingType(
            com.hospital.hospital_billing_system.common.enums.BillingType billingType
    );
}