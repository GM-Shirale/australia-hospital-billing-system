package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.common.enums.ReportStatus;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportResponseDTO;

import java.util.List;

public interface LabReportService {

    LabReportResponseDTO createReport(LabReportRequestDTO request);

    LabReportResponseDTO getReportById(Long id);

    LabReportResponseDTO getReportByReportNumber(String reportNumber);

    List<LabReportResponseDTO> getReportsByLabOrderId(Long labOrderId);

    List<LabReportResponseDTO> getReportsByPatientId(Long patientId);

    List<LabReportResponseDTO> getReportsByStatus(
            ReportStatus status
    );

    LabReportResponseDTO updateReport(
            Long id,
            LabReportRequestDTO request
    );

    void deleteReport(Long id);
}
