package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.laboratory.dto.LabSampleRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabSampleResponseDTO;

import java.util.List;

public interface LabSampleService {
    LabSampleResponseDTO createSample(
            LabSampleRequestDTO request
    );

    LabSampleResponseDTO getSampleById(
            Long id
    );

    List<LabSampleResponseDTO> getAllSamples();

    List<LabSampleResponseDTO> getSamplesByLabOrder(
            Long labOrderId
    );

    LabSampleResponseDTO updateSample(
            Long id,
            LabSampleRequestDTO request
    );

    void deleteSample(
            Long id
    );
}
