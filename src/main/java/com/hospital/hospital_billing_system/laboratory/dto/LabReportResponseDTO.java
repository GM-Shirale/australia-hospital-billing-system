package com.hospital.hospital_billing_system.laboratory.dto;

import com.hospital.hospital_billing_system.common.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabReportResponseDTO {
    private Long id;

    private String reportNumber;

    private Long labOrderId;
    private String orderNumber;

    private Long verificationId;

    private Long labResultId;
    private String resultNumber;

    private Long patientId;

    private LocalDateTime reportDate;

    private ReportStatus status;

    private String reportSummary;

    private String reportFilePath;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
