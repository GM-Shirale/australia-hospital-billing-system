package com.hospital.hospital_billing_system.patient.dto;

import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationStatusRequest {

    private VerificationStatus status;
}