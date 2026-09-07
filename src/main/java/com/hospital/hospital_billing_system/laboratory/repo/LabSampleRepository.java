package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabSample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabSampleRepository extends JpaRepository<LabSample, Long> {

    Optional<LabSample> findBySampleNumber(String sampleNumber);

    Optional<LabSample> findByBarcode(String barcode);

    List<LabSample> findByLabOrderId(Long labOrderId);

    boolean existsBySampleNumber(String sampleNumber);

    boolean existsByBarcode(String barcode);

    boolean existsBySampleNumberAndIdNot(
            String sampleNumber,
            Long id
    );

    boolean existsByBarcodeAndIdNot(String barcode, Long id);


}