package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabResultRepository extends JpaRepository<LabResult,Long> {


    Optional<LabResult> findByResultNumber(String resultNumber);

    List<LabResult> findByLabSampleId(Long labSampleId);
    List<LabResult> findByLabTestId(Long labTestId);

    boolean existsByResultNumber(String resultNumber);

}
