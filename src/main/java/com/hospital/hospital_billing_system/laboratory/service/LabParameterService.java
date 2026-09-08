package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.laboratory.dto.LabParameterRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabParameterResponseDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportResponseDTO;


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

    interface LabReportService  {
        LabReportResponseDTO createReport(LabReportRequestDTO request);

        LabReportResponseDTO getReportById(Long id);

        LabReportResponseDTO getReportByReportNumber(String reportNumber);

        List<LabReportResponseDTO> getReportsByLabOrderId(Long labOrderId);

        List<LabReportResponseDTO> getReportsByPatientId(Long patientId);

        List<LabReportResponseDTO> getReportsByStatus(String status);

        LabReportResponseDTO updateReport(
                Long id,
                LabReportRequestDTO request
        );

        void deleteReport(Long id);
    }
}
