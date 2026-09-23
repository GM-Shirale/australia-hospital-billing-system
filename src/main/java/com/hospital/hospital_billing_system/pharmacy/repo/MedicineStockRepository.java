package com.hospital.hospital_billing_system.pharmacy.repo;

import com.hospital.hospital_billing_system.pharmacy.entity.MedicineStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineStockRepository extends JpaRepository<MedicineStock,Long> {

    Optional<MedicineStock> findByMedicineMedicineIdAndBatchBatchId(
            Long medicineId,
            Long batchId
    );

    boolean existsByMedicineMedicineIdAndBatchBatchId(
            Long medicineId,
            Long batchId
    );

    List<MedicineStock> findByMedicineMedicineId(
            Long medicineId
    );

    List<MedicineStock> findByBatchBatchId(
            Long batchId
    );

    List<MedicineStock> findByActiveTrue();

    List<MedicineStock> findByQuantityAvailableLessThanEqual(
            Integer quantity
    );
}
