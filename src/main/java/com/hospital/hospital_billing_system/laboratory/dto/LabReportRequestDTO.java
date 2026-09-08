package com.hospital.hospital_billing_system.laboratory.dto;

import com.hospital.hospital_billing_system.common.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabReportRequestDTO {

    @NotNull(message = "Lab order ID is required")
    private Long labOrderId;

    @NotNull(message = "Verification Id is required")
    private Long verificationId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Report status is required")
    private ReportStatus status;

    @Size(max = 500,message ="Report file path must not exceed 500 characters")
    private String reportSummary;


    @Size(max = 500,message = "Report file path must not exceed 500 characters")
    private String reportFilePath;

}


