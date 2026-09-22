package com.hospital.hospital_billing_system.pharmacy.repo;

import com.hospital.hospital_billing_system.pharmacy.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long> {


    boolean existsByMedicineCode(String medicineCode);

    boolean existsByMedicineCodeAndMedicineIdNot(
            String medicineCode,
            Long medicineId
    );

    List<Medicine>
    findByMedicineCodeContainingIgnoreCaseOrGenericNameContainingIgnoreCaseOrBrandNameContainingIgnoreCase(
            String medicineCode,
            String genericName,
            String brandName
    );
}
