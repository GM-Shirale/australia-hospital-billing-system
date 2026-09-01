package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabTestRepository extends JpaRepository <LabTest,Long> {


    Optional<LabTest> findByTestCode(String testCode);

    boolean existsByTestCode(String testCode);

}
