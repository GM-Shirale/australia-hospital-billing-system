package com.hospital.hospital_billing_system.laboratory.service.impl;

import com.hospital.hospital_billing_system.common.enums.ReportStatus;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabReportResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.*;
import com.hospital.hospital_billing_system.laboratory.repo.LabOrderRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabReportRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabResultValueRepository;
import com.hospital.hospital_billing_system.laboratory.repo.VerificationRepository;
import com.hospital.hospital_billing_system.laboratory.service.LabReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private final LabResultValueRepository labResultValueRepository;

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
    @Transactional(readOnly = true)
    public byte[] generateReportPdf(Long reportId) {

        log.info("Generating PDF for lab report ID: {}", reportId);

        LabReport report = labReportRepository.findById(reportId)
                .orElseThrow(() -> {
                    log.warn("Lab report not found with ID: {}", reportId);

                    return new ResourceNotFoundException(
                            "Lab report not found with ID: " + reportId
                    );
                });

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream =
                         new PDPageContentStream(document, page)) {

                float y = 780;

                // ==========================================
                // HOSPITAL TITLE
                // ==========================================

                contentStream.beginText();

                contentStream.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA_BOLD
                        ),
                        18
                );

                contentStream.newLineAtOffset(50, y);
                contentStream.showText("AUSTRALIAN HOSPITAL");

                contentStream.endText();

                y -= 30;

                // ==========================================
                // LABORATORY TITLE
                // ==========================================

                contentStream.beginText();

                contentStream.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA_BOLD
                        ),
                        14
                );

                contentStream.newLineAtOffset(50, y);
                contentStream.showText("LABORATORY REPORT");

                contentStream.endText();

                y -= 35;

                // ==========================================
                // REPORT INFORMATION
                // ==========================================

                y = writeLine(
                        contentStream,
                        "Report Number: "
                                + report.getReportNumber(),
                        y
                );

                y = writeLine(
                        contentStream,
                        "Report Date: "
                                + report.getReportDate(),
                        y
                );

                y = writeLine(
                        contentStream,
                        "Report Status: "
                                + report.getStatus(),
                        y
                );

                y = writeLine(
                        contentStream,
                        "Patient ID: "
                                + report.getPatientId(),
                        y
                );

                y -= 15;

                // ==========================================
                // LAB ORDER INFORMATION
                // ==========================================

                y = writeHeading(
                        contentStream,
                        "LAB ORDER INFORMATION",
                        y
                );

                y = writeLine(
                        contentStream,
                        "Lab Order ID: "
                                + report.getLabOrder().getId(),
                        y
                );

                y = writeLine(
                        contentStream,
                        "Order Number: "
                                + report.getLabOrder().getOrderNumber(),
                        y
                );

                y -= 15;

                // ==========================================
                // LABORATORY TEST
                // ==========================================

                y = writeHeading(
                        contentStream,
                        "LABORATORY TEST",
                        y
                );

                /*
                 * Get the LabTest from the LabResult.
                 *
                 * The Verification already points to LabResult.
                 */
                LabResult labResult =
                        report.getVerification().getLabResult();

                if (labResult != null && labResult.getLabTest() != null) {

                    LabTest labTest = labResult.getLabTest();

                    y = writeLine(
                            contentStream,
                            "Test Name: "
                                    + labTest.getTestName(),
                            y
                    );

                    y = writeLine(
                            contentStream,
                            "Test Code: "
                                    + labTest.getTestCode(),
                            y
                    );

                    y = writeLine(
                            contentStream,
                            "Sample Type: "
                                    + labTest.getSampleType(),
                            y
                    );

                } else {

                    y = writeLine(
                            contentStream,
                            "No laboratory test information available.",
                            y
                    );
                }

                y -= 15;

                // ==========================================
                // LABORATORY TEST RESULTS
                // ==========================================

                y = writeHeading(
                        contentStream,
                        "LABORATORY TEST RESULTS",
                        y
                );

                if (labResult != null) {

                    List<LabResultValue> resultValues =
                            labResultValueRepository
                                    .findByLabResultId(
                                            labResult.getId()
                                    );

                    if (resultValues.isEmpty()) {

                        y = writeLine(
                                contentStream,
                                "No result values available.",
                                y
                        );

                    } else {

                        // Table header
                        y = writeResultTableHeader(
                                contentStream,
                                y
                        );

                        // Result rows
                        for (LabResultValue resultValue
                                : resultValues) {

                            y = writeResultTableRow(
                                    contentStream,
                                    resultValue,
                                    y
                            );

                            // Prevent content from going
                            // beyond the bottom of the page.
                            if (y < 80) {

                                contentStream.close();

                                page = new PDPage(PDRectangle.A4);
                                document.addPage(page);

                                // A new content stream would be
                                // required here for multi-page PDFs.
                                break;
                            }
                        }
                    }

                } else {

                    y = writeLine(
                            contentStream,
                            "No laboratory result available.",
                            y
                    );
                }

                y -= 15;

                // ==========================================
                // VERIFICATION
                // ==========================================

                y = writeHeading(
                        contentStream,
                        "VERIFICATION",
                        y
                );

                y = writeLine(
                        contentStream,
                        "Verification ID: "
                                + report.getVerification().getId(),
                        y
                );

                if (report.getVerification().getStatus() != null) {

                    y = writeLine(
                            contentStream,
                            "Verification Status: "
                                    + report.getVerification().getStatus(),
                            y
                    );
                }

                y -= 15;

                // ==========================================
                // REPORT SUMMARY
                // ==========================================

                y = writeHeading(
                        contentStream,
                        "REPORT SUMMARY",
                        y
                );

                String summary = report.getReportSummary();

                if (summary != null && !summary.isBlank()) {

                    y = writeLine(
                            contentStream,
                            summary,
                            y
                    );

                } else {

                    y = writeLine(
                            contentStream,
                            "No report summary available.",
                            y
                    );
                }

                y -= 30;

                // ==========================================
                // FOOTER
                // ==========================================

                contentStream.beginText();

                contentStream.setFont(
                        new PDType1Font(
                                Standard14Fonts.FontName.HELVETICA
                        ),
                        9
                );

                contentStream.newLineAtOffset(50, y);

                contentStream.showText(
                        "This laboratory report was electronically generated "
                                + "by the Australian Hospital Billing System."
                );

                contentStream.endText();
            }

            document.save(outputStream);

            log.info(
                    "Lab report PDF generated successfully. Report ID: {}",
                    reportId
            );

            return outputStream.toByteArray();

        } catch (IOException exception) {

            log.error(
                    "Failed to generate PDF for lab report ID: {}",
                    reportId,
                    exception
            );

            throw new IllegalStateException(
                    "Failed to generate laboratory report PDF",
                    exception
            );
        }
    }
    private float writeHeading(
            PDPageContentStream contentStream,
            String text,
            float y
    ) throws IOException {

        contentStream.beginText();

        contentStream.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                ),
                12
        );

        contentStream.newLineAtOffset(50, y);
        contentStream.showText(text);

        contentStream.endText();

        return y - 20;
    }
    private float writeLine(
            PDPageContentStream contentStream,
            String text,
            float y
    ) throws IOException {

        contentStream.beginText();

        contentStream.setFont(
                new PDType1Font(Standard14Fonts.FontName.HELVETICA),
                11
        );

        contentStream.newLineAtOffset(50, y);

        contentStream.showText(
                text != null ? text : ""
        );

        contentStream.endText();

        return y - 20;
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
    private float writeResultTableHeader(
            PDPageContentStream contentStream,
            float y
    ) throws IOException {

        contentStream.beginText();

        contentStream.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA_BOLD
                ),
                9
        );

        contentStream.newLineAtOffset(50, y);

        contentStream.showText("Parameter");
        contentStream.newLineAtOffset(120, 0);
        contentStream.showText("Result");
        contentStream.newLineAtOffset(80, 0);
        contentStream.showText("Unit");
        contentStream.newLineAtOffset(80, 0);
        contentStream.showText("Reference Range");

        contentStream.endText();

        return y - 18;
    }
    private float writeResultTableRow(
            PDPageContentStream contentStream,
            LabResultValue resultValue,
            float y
    ) throws IOException {

        String parameterName = "";

        if (resultValue.getLabParameter() != null) {

            parameterName =
                    resultValue.getLabParameter().getParameterName();
        }

        String result =
                resultValue.getResultValue() != null
                        ? resultValue.getResultValue()
                        : "";

        String unit =
                resultValue.getUnit() != null
                        ? resultValue.getUnit()
                        : "";

        String referenceRange =
                resultValue.getReferenceRange() != null
                        ? resultValue.getReferenceRange()
                        : "";

        contentStream.beginText();

        contentStream.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.HELVETICA
                ),
                9
        );

        contentStream.newLineAtOffset(50, y);

        contentStream.showText(parameterName);

        contentStream.newLineAtOffset(120, 0);
        contentStream.showText(result);

        contentStream.newLineAtOffset(80, 0);
        contentStream.showText(unit);

        contentStream.newLineAtOffset(80, 0);
        contentStream.showText(referenceRange);

        contentStream.endText();

        return y - 18;
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