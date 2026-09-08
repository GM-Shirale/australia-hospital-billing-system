package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import com.hospital.hospital_billing_system.laboratory.dto.VerificationRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.VerificationResponseDTO;

import java.util.List;

public interface VerificationService {

    VerificationResponseDTO createVerification(
            VerificationRequestDTO request
    );

    VerificationResponseDTO getVerificationById(Long id);

    VerificationResponseDTO getVerificationByLabResultId(
            Long labResultId
    );

    List<VerificationResponseDTO> getVerificationsByStatus(
            VerificationStatus status
    );

    List<VerificationResponseDTO> getVerificationsByVerifiedBy(
            Long verifiedBy
    );

    VerificationResponseDTO updateVerification(
            Long id,
            VerificationRequestDTO request
    );

    void deleteVerification(Long id);
}