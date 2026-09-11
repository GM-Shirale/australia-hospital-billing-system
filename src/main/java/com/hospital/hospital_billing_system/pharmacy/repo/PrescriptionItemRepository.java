package com.hospital.hospital_billing_system.pharmacy.repo;

import com.hospital.hospital_billing_system.pharmacy.entity.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem,Long> {

    List<PrescriptionItem> findByPrescriptionPrescriptionId(
            Long prescriptionId
    );

    Optional<PrescriptionItem> findByPrescriptionPrescriptionIdAndMedicineMedicineId(
            Long prescriptionId,
            Long medicineId
    );

    boolean existsByPrescriptionPrescriptionIdAndMedicineMedicineId(
            Long prescriptionId,
            Long medicineId
    );

    List<PrescriptionItem> findByMedicineMedicineId(
            Long medicineId
    );
}
