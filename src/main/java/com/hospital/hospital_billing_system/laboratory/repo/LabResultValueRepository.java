package com.hospital.hospital_billing_system.laboratory.repo;


import com.hospital.hospital_billing_system.laboratory.entity.LabResultValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabResultValueRepository extends JpaRepository<LabResultValue, Long> {
    List<LabResultValue> findByLabResultId(Long labResultId);

    List<LabResultValue> findByLabParameterId(Long labParameterId);

    boolean existsByLabResultIdAndLabParameterId(
            Long labResultId,
            Long labParameterId
    );
}
