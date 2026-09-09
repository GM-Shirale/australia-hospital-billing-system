package com.hospital.hospital_billing_system.admission.dto;


import com.hospital.hospital_billing_system.admission.entity.AdmissionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionResponse {

    private Long admissionId;

    private String admissionNumber;

    private Long patientId;

    private LocalDateTime admissionDate;

    private LocalDateTime dischargeDate;

    private String admissionReason;

    private AdmissionStatus status;
}
