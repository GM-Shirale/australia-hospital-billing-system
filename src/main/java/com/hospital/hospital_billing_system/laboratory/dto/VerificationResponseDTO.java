package com.hospital.hospital_billing_system.laboratory.dto;

import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResponseDTO {

    private Long id;

    private Long labResultId;

    private String resultNumber;

    private Long verifiedBy;

    private LocalDateTime verifiedAt;

    private VerificationStatus status;

    private String comments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}