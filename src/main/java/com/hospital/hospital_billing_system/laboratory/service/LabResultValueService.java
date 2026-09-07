package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.laboratory.dto.LabResultValueRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabResultValueResponseDTO;

import java.util.List;

public interface LabResultValueService {

    LabResultValueResponseDTO createResultValue(
            LabResultValueRequestDTO request
    );

    LabResultValueResponseDTO getResultValueById(
            Long id
    );

    List<LabResultValueResponseDTO> getAllResultValues();

    List<LabResultValueResponseDTO> getResultValuesByResult(
            Long labResultId
    );

    List<LabResultValueResponseDTO> getResultValuesByParameter(
            Long labParameterId
    );

    LabResultValueResponseDTO updateResultValue(
            Long id,
            LabResultValueRequestDTO request
    );

    void deleteResultValue(Long id);
}