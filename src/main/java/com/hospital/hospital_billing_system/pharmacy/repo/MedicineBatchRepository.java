package com.hospital.hospital_billing_system.pharmacy.repo;

import com.hospital.hospital_billing_system.common.enums.MedicineBatchStatus;
import com.hospital.hospital_billing_system.pharmacy.entity.MedicineBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineBatchRepository extends JpaRepository<MedicineBatch, Long> {

    boolean existsByMedicineMedicineIdAndBatchNumber(
            Long medicineId,
            String batchNumber
    );

    boolean existsByMedicineMedicineIdAndBatchNumberAndBatchIdNot(
            Long medicineId,
            String batchNumber,
            Long batchId
    );

    List<MedicineBatch> findByMedicineMedicineId(Long medicineId);

    List<MedicineBatch> findByStatus(MedicineBatchStatus status);

    List<MedicineBatch> findByExpiryDateBefore(LocalDate date);

    List<MedicineBatch> findByQuantityGreaterThan(Integer quantity);
}