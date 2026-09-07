package com.hospital.hospital_billing_system.laboratory.service.impl;

import com.hospital.hospital_billing_system.common.enums.ResultStatus;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabResultRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabResultResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabResult;
import com.hospital.hospital_billing_system.laboratory.entity.LabSample;
import com.hospital.hospital_billing_system.laboratory.entity.LabTest;
import com.hospital.hospital_billing_system.laboratory.repo.LabOrderItemRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabResultRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabSampleRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabTestRepository;
import com.hospital.hospital_billing_system.laboratory.service.LabResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabResultServiceImpl implements LabResultService {

    private final LabOrderItemRepository labOrderItemRepository;
    private final LabResultRepository labResultRepository;
    private final LabSampleRepository labSampleRepository;
    private final LabTestRepository labTestRepository;

    @Override
    public LabResultResponseDTO createResult(LabResultRequestDTO request) {

        log.info(
                "Creating lab result. Sample ID: {}, Test ID: {}",
                request.getLabSampleId(),
                request.getLabTestId()
        );

        LabSample labSample =
                findLabSample(request.getLabSampleId());

        LabTest labTest =
                findLabTest(request.getLabTestId());

        validateSampleAndTest(labSample, labTest);

        LabResult result = LabResult.builder()
                .resultNumber(generateResultNumber())
                .labSample(labSample)
                .labTest(labTest)
                .status(ResultStatus.PENDING)
                .comments(request.getComments())
                .build();

        LabResult savedResult =
                labResultRepository.save(result);

        log.info(
                "Lab result created successfully. ID: {}",
                savedResult.getId()
        );

        return mapToResponse(savedResult);
    }

    @Override
    @Transactional(readOnly = true)
    public LabResultResponseDTO getResultById(Long id) {

        log.debug("Fetching lab result with ID: {}", id);

        LabResult result =
                labResultRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab result not found with ID: " + id
                                )
                        );

        return mapToResponse(result);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultResponseDTO> getAllResults() {

        log.debug("Fetching all lab results");

        return labResultRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultResponseDTO> getResultsBySample(
            Long labSampleId) {

        log.debug(
                "Fetching results for Lab Sample ID: {}",
                labSampleId
        );

        findLabSample(labSampleId);

        return labResultRepository
                .findByLabSampleId(labSampleId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultResponseDTO> getResultsByTest(
            Long labTestId) {

        log.debug(
                "Fetching results for Lab Test ID: {}",
                labTestId
        );

        findLabTest(labTestId);

        return labResultRepository
                .findByLabTestId(labTestId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LabResultResponseDTO updateResult(
            Long id,
            LabResultRequestDTO request) {

        log.info("Updating lab result. ID: {}", id);

        LabResult result =
                labResultRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab result not found with ID: " + id
                                )
                        );

        LabSample labSample =
                findLabSample(request.getLabSampleId());

        LabTest labTest =
                findLabTest(request.getLabTestId());

        validateSampleAndTest(labSample, labTest);

        result.setLabSample(labSample);
        result.setLabTest(labTest);
        result.setComments(request.getComments());

        LabResult updatedResult =
                labResultRepository.save(result);

        log.info(
                "Lab result updated successfully. ID: {}",
                updatedResult.getId()
        );

        return mapToResponse(updatedResult);
    }

    @Override
    public void deleteResult(Long id) {

        log.info("Deleting lab result. ID: {}", id);

        LabResult result =
                labResultRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab result not found with ID: " + id
                                )
                        );

        labResultRepository.delete(result);

        log.info(
                "Lab result deleted successfully. ID: {}",
                id
        );
    }

    private LabSample findLabSample(Long labSampleId) {

        return labSampleRepository.findById(labSampleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab sample not found with ID: "
                                        + labSampleId
                        )
                );
    }

    private LabTest findLabTest(Long labTestId) {

        return labTestRepository.findById(labTestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab test not found with ID: "
                                        + labTestId
                        )
                );
    }

    private void validateSampleAndTest(
            LabSample labSample,
            LabTest labTest) {

        Long labOrderId =
                labSample.getLabOrder().getId();

        boolean testBelongsToOrder =
                labOrderItemRepository
                        .existsByLabOrderIdAndLabTestId(
                                labOrderId,
                                labTest.getId()
                        );

        if (!testBelongsToOrder) {
            throw new IllegalArgumentException(
                    "Lab test is not associated with the lab order of this sample"
            );
        }
    }

    private String generateResultNumber() {

        String prefix = "LAB-RES-";

        String date =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern("yyyyMMdd")
                        );

        String resultNumber;

        do {

            resultNumber =
                    prefix
                            + date
                            + "-"
                            + String.format(
                            "%04d",
                            (int) (Math.random() * 10000)
                    );

        } while (
                labResultRepository
                        .existsByResultNumber(resultNumber)
        );

        return resultNumber;
    }

    private LabResultResponseDTO mapToResponse(
            LabResult result) {

        return LabResultResponseDTO.builder()
                .id(result.getId())
                .resultNumber(result.getResultNumber())

                .labSampleId(
                        result.getLabSample().getId()
                )

                .sampleNumber(
                        result.getLabSample().getSampleNumber()
                )

                .labOrderId(
                        result.getLabSample()
                                .getLabOrder()
                                .getId()
                )

                .orderNumber(
                        result.getLabSample()
                                .getLabOrder()
                                .getOrderNumber()
                )

                .labTestId(
                        result.getLabTest().getId()
                )

                .testCode(
                        result.getLabTest().getTestCode()
                )

                .testName(
                        result.getLabTest().getTestName()
                )

                .status(
                        result.getStatus().name()
                )

                .resultDate(
                        result.getResultDate()
                )

                .comments(
                        result.getComments()
                )

                .createdAt(
                        result.getCreatedAt()
                )

                .updatedAt(
                        result.getUpdatedAt()
                )

                .build();
    }
}