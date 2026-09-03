package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.laboratory.dto.LabParameterRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabParameterResponseDTO;


import java.util.List;


public interface LabParameterService {

    LabParameterResponseDTO createParameter(
            LabParameterRequestDTO request);

    LabParameterResponseDTO getParameterById(
            Long id
    );

    List<LabParameterResponseDTO> getAllParameters();

    List<LabParameterResponseDTO> getParametersByLabTest(
            Long labTestId
    );

    LabParameterResponseDTO updateParameters(
            Long id,
            LabParameterRequestDTO request);

    void deleteParameters(Long id);
}
