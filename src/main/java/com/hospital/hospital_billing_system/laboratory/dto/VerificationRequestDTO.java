package com.hospital.hospital_billing_system.laboratory.dto;

import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
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
public class VerificationRequestDTO {

    @NotNull(message = "Lab result ID is required")
    private Long labResultId;

    @NotNull(message = "Verified by user ID is required")
    private Long verifiedBy;

    @NotNull(message = "Verification status is required")
    private VerificationStatus status;

    @Size(
            max = 500,
            message = "Comments must not exceed 500 characters"
    )
    private String comments;
}