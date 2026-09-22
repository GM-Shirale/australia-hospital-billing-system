package com.hospital.hospital_billing_system.laboratory.service.impl;

import com.hospital.hospital_billing_system.common.enums.VerificationStatus;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.VerificationRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.VerificationResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabResult;
import com.hospital.hospital_billing_system.laboratory.entity.Verification;
import com.hospital.hospital_billing_system.laboratory.repo.LabResultRepository;
import com.hospital.hospital_billing_system.laboratory.repo.VerificationRepository;
import com.hospital.hospital_billing_system.laboratory.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VerificationServiceImpl
        implements VerificationService {

    private final VerificationRepository verificationRepository;
    private final LabResultRepository labResultRepository;

    @Override
    public VerificationResponseDTO createVerification(
            VerificationRequestDTO request) {

        log.info(
                "Creating verification for lab result ID: {}",
                request.getLabResultId()
        );

        LabResult labResult = labResultRepository.findById(
                request.getLabResultId()
        ).orElseThrow(() -> {
            log.warn(
                    "Lab result not found with ID: {}",
                    request.getLabResultId()
            );

            return new ResourceNotFoundException(
                    "Lab result not found with ID: "
                            + request.getLabResultId()
            );
        });

        // One verification per lab result
        if (verificationRepository
                .findByLabResultId(request.getLabResultId())
                .isPresent()) {

            log.warn(
                    "Verification already exists for lab result ID: {}",
                    request.getLabResultId()
            );

            throw new IllegalArgumentException(
                    "Verification already exists for lab result ID: "
                            + request.getLabResultId()
            );
        }

        Verification verification = Verification.builder()
                .labResult(labResult)
                .verifiedBy(request.getVerifiedBy())
                .status(request.getStatus())
                .comments(request.getComments())
                .build();

        if (request.getStatus() == VerificationStatus.VERIFIED
                || request.getStatus() == VerificationStatus.REJECTED) {

            verification.setVerifiedAt(LocalDateTime.now());
        }

        Verification savedVerification =
                verificationRepository.save(verification);

        log.info(
                "Verification created successfully. Verification ID: {}, Lab Result ID: {}, Status: {}",
                savedVerification.getId(),
                request.getLabResultId(),
                savedVerification.getStatus()
        );

        return mapToResponseDTO(savedVerification);
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationResponseDTO getVerificationById(
            Long id) {

        log.debug(
                "Fetching verification by ID: {}",
                id
        );

        Verification verification =
                verificationRepository.findById(id)
                        .orElseThrow(() -> {
                            log.warn(
                                    "Verification not found with ID: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Verification not found with ID: "
                                            + id
                            );
                        });

        return mapToResponseDTO(verification);
    }

    @Override
    @Transactional(readOnly = true)
    public VerificationResponseDTO getVerificationByLabResultId(
            Long labResultId) {

        log.debug(
                "Fetching verification for lab result ID: {}",
                labResultId
        );

        Verification verification =
                verificationRepository
                        .findByLabResultId(labResultId)
                        .orElseThrow(() -> {
                            log.warn(
                                    "Verification not found for lab result ID: {}",
                                    labResultId
                            );

                            return new ResourceNotFoundException(
                                    "Verification not found for lab result ID: "
                                            + labResultId
                            );
                        });

        return mapToResponseDTO(verification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificationResponseDTO> getVerificationsByStatus(
            VerificationStatus status) {

        log.debug(
                "Fetching verifications by status: {}",
                status
        );

        return verificationRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VerificationResponseDTO> getVerificationsByVerifiedBy(
            Long verifiedBy) {

        log.debug(
                "Fetching verifications by verifiedBy: {}",
                verifiedBy
        );

        return verificationRepository
                .findByVerifiedBy(verifiedBy)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public VerificationResponseDTO updateVerification(
            Long id,
            VerificationRequestDTO request) {

        log.info(
                "Updating verification ID: {}",
                id
        );

        Verification verification =
                verificationRepository.findById(id)
                        .orElseThrow(() -> {
                            log.warn(
                                    "Verification not found with ID: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Verification not found with ID: "
                                            + id
                            );
                        });

        LabResult labResult = labResultRepository.findById(
                request.getLabResultId()
        ).orElseThrow(() -> {
            log.warn(
                    "Lab result not found with ID: {}",
                    request.getLabResultId()
            );

            return new ResourceNotFoundException(
                    "Lab result not found with ID: "
                            + request.getLabResultId()
            );
        });

        verificationRepository
                .findByLabResultId(request.getLabResultId())
                .ifPresent(existingVerification -> {

                    if (!existingVerification.getId().equals(id)) {

                        log.warn(
                                "Another verification already exists for lab result ID: {}",
                                request.getLabResultId()
                        );

                        throw new IllegalArgumentException(
                                "Another verification already exists "
                                        + "for lab result ID: "
                                        + request.getLabResultId()
                        );
                    }
                });

        verification.setLabResult(labResult);
        verification.setVerifiedBy(request.getVerifiedBy());
        verification.setStatus(request.getStatus());
        verification.setComments(request.getComments());

        if (request.getStatus() == VerificationStatus.VERIFIED
                || request.getStatus() == VerificationStatus.REJECTED) {

            if (verification.getVerifiedAt() == null) {
                verification.setVerifiedAt(LocalDateTime.now());
            }

        } else {
            verification.setVerifiedAt(null);
        }

        Verification updatedVerification =
                verificationRepository.save(verification);

        log.info(
                "Verification updated successfully. Verification ID: {}, Status: {}",
                updatedVerification.getId(),
                updatedVerification.getStatus()
        );

        return mapToResponseDTO(updatedVerification);
    }

    @Override
    public void deleteVerification(Long id) {

        log.info(
                "Deleting verification ID: {}",
                id
        );

        Verification verification =
                verificationRepository.findById(id)
                        .orElseThrow(() -> {
                            log.warn(
                                    "Verification not found with ID: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Verification not found with ID: "
                                            + id
                            );
                        });

        verificationRepository.delete(verification);

        log.info(
                "Verification deleted successfully. Verification ID: {}",
                id
        );
    }

    private VerificationResponseDTO mapToResponseDTO(
            Verification verification) {

        return VerificationResponseDTO.builder()
                .id(verification.getId())
                .labResultId(
                        verification.getLabResult().getId()
                )
                .resultNumber(
                        verification.getLabResult().getResultNumber()
                )
                .verifiedBy(
                        verification.getVerifiedBy()
                )
                .verifiedAt(
                        verification.getVerifiedAt()
                )
                .status(
                        verification.getStatus()
                )
                .comments(
                        verification.getComments()
                )
                .createdAt(
                        verification.getCreatedAt()
                )
                .updatedAt(
                        verification.getUpdatedAt()
                )
                .build();
    }
}