package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.laboratory.dto.LabResultRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabResultResponseDTO;

import java.util.List;

public interface LabResultService {

    LabResultResponseDTO createResult(LabResultRequestDTO request);

    LabResultResponseDTO getResultById(Long id);

    List<LabResultResponseDTO> getAllResults();

    List<LabResultResponseDTO> getResultsBySample(Long labSampleId);

    List<LabResultResponseDTO> getResultsByTest(Long labTestId);

    LabResultResponseDTO updateResult(Long id, LabResultRequestDTO request);

    void deleteResult(Long id);
}