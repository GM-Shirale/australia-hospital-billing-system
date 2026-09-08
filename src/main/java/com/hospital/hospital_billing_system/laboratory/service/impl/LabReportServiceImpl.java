package com.hospital.hospital_billing_system.laboratory.service.impl;

import com.hospital.hospital_billing_system.common.enums.ReportStatus;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabOrder;
import com.hospital.hospital_billing_system.laboratory.entity.LabReport;
import com.hospital.hospital_billing_system.laboratory.entity.Verification;
import com.hospital.hospital_billing_system.laboratory.repo.LabOrderRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabReportRepository;
import com.hospital.hospital_billing_system.laboratory.repo.VerificationRepository;
import com.hospital.hospital_billing_system.laboratory.service.LabReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabReportServiceImpl implements LabReportService {

    private final LabReportRepository labReportRepository;
    private final LabOrderRepository labOrderRepository;
    private final VerificationRepository verificationRepository;

    @Override
    public LabReportResponseDTO createReport(
            LabReportRequestDTO request) {

        log.info(
                "Creating lab report for lab order ID: {}, verification ID: {}",
                request.getLabOrderId(),
                request.getVerificationId()
        );

        LabOrder labOrder = labOrderRepository.findById(
                request.getLabOrderId()
        ).orElseThrow(() -> {
            log.warn(
                    "Lab order not found with ID: {}",
                    request.getLabOrderId()
            );

            return new ResourceNotFoundException(
                    "Lab order not found with ID: "
                            + request.getLabOrderId()
            );
        });

        Verification verification = verificationRepository.findById(
                request.getVerificationId()
        ).orElseThrow(() -> {
            log.warn(
                    "Verification not found with ID: {}",
                    request.getVerificationId()
            );

            return new ResourceNotFoundException(
                    "Verification not found with ID: "
                            + request.getVerificationId()
            );
        });

        validateVerificationBelongsToOrder(
                verification,
                labOrder
        );

        LabReport labReport = LabReport.builder()
                .reportNumber(generateReportNumber())
                .labOrder(labOrder)
                .verification(verification)
                .patientId(request.getPatientId())
                .status(request.getStatus())
                .reportSummary(request.getReportSummary())
                .reportFilePath(request.getReportFilePath())
                .build();

        LabReport savedReport =
                labReportRepository.save(labReport);

        log.info(
                "Lab report created successfully. Report ID: {}, Report Number: {}",
                savedReport.getId(),
                savedReport.getReportNumber()
        );

        return mapToResponseDTO(savedReport);
    }

    @Override
    @Transactional(readOnly = true)
    public LabReportResponseDTO getReportById(Long id) {

        log.debug(
                "Fetching lab report by ID: {}",
                id
        );

        LabReport labReport = labReportRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(
                            "Lab report not found with ID: {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Lab report not found with ID: " + id
                    );
                });

        return mapToResponseDTO(labReport);
    }

    @Override
    @Transactional(readOnly = true)
    public LabReportResponseDTO getReportByReportNumber(
            String reportNumber) {

        log.debug(
                "Fetching lab report by report number: {}",
                reportNumber
        );

        LabReport labReport =
                labReportRepository.findByReportNumber(reportNumber)
                        .orElseThrow(() -> {
                            log.warn(
                                    "Lab report not found with report number: {}",
                                    reportNumber
                            );

                            return new ResourceNotFoundException(
                                    "Lab report not found with report number: "
                                            + reportNumber
                            );
                        });

        return mapToResponseDTO(labReport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabReportResponseDTO> getReportsByLabOrderId(
            Long labOrderId) {

        log.debug(
                "Fetching lab reports for lab order ID: {}",
                labOrderId
        );

        return labReportRepository
                .findByLabOrderId(labOrderId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabReportResponseDTO> getReportsByPatientId(
            Long patientId) {

        log.debug(
                "Fetching lab reports for patient ID: {}",
                patientId
        );

        return labReportRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabReportResponseDTO> getReportsByStatus(
            ReportStatus status) {

        log.debug(
                "Fetching lab reports by status: {}",
                status
        );

        return labReportRepository
                .findByStatus(status)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public LabReportResponseDTO updateReport(
            Long id,
            LabReportRequestDTO request) {

        log.info(
                "Updating lab report ID: {}",
                id
        );

        LabReport labReport = labReportRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(
                            "Lab report not found with ID: {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Lab report not found with ID: " + id
                    );
                });

        LabOrder labOrder = labOrderRepository.findById(
                request.getLabOrderId()
        ).orElseThrow(() -> {
            log.warn(
                    "Lab order not found with ID: {}",
                    request.getLabOrderId()
            );

            return new ResourceNotFoundException(
                    "Lab order not found with ID: "
                            + request.getLabOrderId()
            );
        });

        Verification verification =
                verificationRepository.findById(
                        request.getVerificationId()
                ).orElseThrow(() -> {
                    log.warn(
                            "Verification not found with ID: {}",
                            request.getVerificationId()
                    );

                    return new ResourceNotFoundException(
                            "Verification not found with ID: "
                                    + request.getVerificationId()
                    );
                });

        validateVerificationBelongsToOrder(
                verification,
                labOrder
        );

        labReport.setLabOrder(labOrder);
        labReport.setVerification(verification);
        labReport.setPatientId(request.getPatientId());
        labReport.setStatus(request.getStatus());
        labReport.setReportSummary(request.getReportSummary());
        labReport.setReportFilePath(request.getReportFilePath());

        LabReport updatedReport =
                labReportRepository.save(labReport);

        log.info(
                "Lab report updated successfully. Report ID: {}, Report Number: {}",
                updatedReport.getId(),
                updatedReport.getReportNumber()
        );

        return mapToResponseDTO(updatedReport);
    }

    @Override
    public void deleteReport(Long id) {

        log.info(
                "Deleting lab report ID: {}",
                id
        );

        LabReport labReport = labReportRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn(
                            "Lab report not found with ID: {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Lab report not found with ID: " + id
                    );
                });

        labReportRepository.delete(labReport);

        log.info(
                "Lab report deleted successfully. Report ID: {}",
                id
        );
    }

    private void validateVerificationBelongsToOrder(
            Verification verification,
            LabOrder labOrder) {

        if (verification.getLabResult() == null
                || verification.getLabResult().getLabSample() == null
                || verification.getLabResult()
                .getLabSample()
                .getLabOrder() == null) {

            log.warn(
                    "Verification ID {} is not properly associated with a lab order",
                    verification.getId()
            );

            throw new IllegalArgumentException(
                    "Verification is not properly associated with a lab order"
            );
        }

        Long verificationOrderId =
                verification.getLabResult()
                        .getLabSample()
                        .getLabOrder()
                        .getId();

        if (!labOrder.getId().equals(verificationOrderId)) {

            log.warn(
                    "Verification ID {} does not belong to lab order ID {}",
                    verification.getId(),
                    labOrder.getId()
            );

            throw new IllegalArgumentException(
                    "Verification does not belong to the specified lab order"
            );
        }
    }

    private String generateReportNumber() {

        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        long sequence =
                labReportRepository.count() + 1;

        String reportNumber = String.format(
                "LAB-REP-%s-%04d",
                date,
                sequence
        );

        log.debug(
                "Generated lab report number: {}",
                reportNumber
        );

        return reportNumber;
    }

    private LabReportResponseDTO mapToResponseDTO(
            LabReport labReport) {

        return LabReportResponseDTO.builder()
                .id(labReport.getId())
                .reportNumber(labReport.getReportNumber())
                .labOrderId(
                        labReport.getLabOrder().getId()
                )
                .orderNumber(
                        labReport.getLabOrder().getOrderNumber()
                )
                .verificationId(
                        labReport.getVerification().getId()
                )
                .labResultId(
                        labReport.getVerification()
                                .getLabResult()
                                .getId()
                )
                .resultNumber(
                        labReport.getVerification()
                                .getLabResult()
                                .getResultNumber()
                )
                .patientId(labReport.getPatientId())
                .reportDate(labReport.getReportDate())
                .status(labReport.getStatus())
                .reportSummary(labReport.getReportSummary())
                .reportFilePath(labReport.getReportFilePath())
                .createdAt(labReport.getCreatedAt())
                .updatedAt(labReport.getUpdatedAt())
                .build();
    }
}