package com.hospital.hospital_billing_system.laboratory.service.impl;

import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabResultValueRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabResultValueResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabParameter;
import com.hospital.hospital_billing_system.laboratory.entity.LabResult;
import com.hospital.hospital_billing_system.laboratory.entity.LabResultValue;
import com.hospital.hospital_billing_system.laboratory.repo.LabResultRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabResultValueRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LapParameterRepository;
import com.hospital.hospital_billing_system.laboratory.service.LabResultValueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabResultValueServiceImpl
        implements LabResultValueService {

    private final LabResultValueRepository labResultValueRepository;
    private final LabResultRepository labResultRepository;
    private final LapParameterRepository lapParameterRepository;

    @Override
    public LabResultValueResponseDTO createResultValue(
            LabResultValueRequestDTO request) {

        log.info(
                "Creating result value. Result ID: {}, Parameter ID: {}",
                request.getLabResultId(),
                request.getLabParameterId()
        );

        LabResult labResult =
                findLabResult(request.getLabResultId());

        LabParameter labParameter =
                findLabParameter(request.getLabParameterId());

        validateParameterBelongsToResult(
                labResult,
                labParameter
        );

        validateDuplicateParameter(
                request.getLabResultId(),
                request.getLabParameterId()
        );

        LabResultValue resultValue =
                LabResultValue.builder()
                        .labResult(labResult)
                        .labParameter(labParameter)
                        .resultValue(request.getResultValue())
                        .unit(request.getUnit())
                        .referenceRange(request.getReferenceRange())
                        .abnormal(
                                request.getAbnormal() != null
                                        ? request.getAbnormal()
                                        : false
                        )
                        .comments(request.getComments())
                        .build();

        LabResultValue savedResultValue =
                labResultValueRepository.save(resultValue);

        log.info(
                "Lab result value created successfully. ID: {}",
                savedResultValue.getId()
        );

        return mapToResponse(savedResultValue);
    }

    @Override
    @Transactional(readOnly = true)
    public LabResultValueResponseDTO getResultValueById(
            Long id) {

        log.debug(
                "Fetching lab result value with ID: {}",
                id
        );

        LabResultValue resultValue =
                findResultValue(id);

        return mapToResponse(resultValue);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultValueResponseDTO> getAllResultValues() {

        log.debug("Fetching all lab result values");

        return labResultValueRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultValueResponseDTO> getResultValuesByResult(
            Long labResultId) {

        log.debug(
                "Fetching result values for Lab Result ID: {}",
                labResultId
        );

        findLabResult(labResultId);

        return labResultValueRepository
                .findByLabResultId(labResultId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabResultValueResponseDTO> getResultValuesByParameter(
            Long labParameterId) {

        log.debug(
                "Fetching result values for Lab Parameter ID: {}",
                labParameterId
        );

        findLabParameter(labParameterId);

        return labResultValueRepository
                .findByLabParameterId(labParameterId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LabResultValueResponseDTO updateResultValue(
            Long id,
            LabResultValueRequestDTO request) {

        log.info(
                "Updating lab result value. ID: {}",
                id
        );

        LabResultValue resultValue =
                findResultValue(id);

        LabResult labResult =
                findLabResult(request.getLabResultId());

        LabParameter labParameter =
                findLabParameter(request.getLabParameterId());

        validateParameterBelongsToResult(
                labResult,
                labParameter
        );

        /*
         * Check duplicate only when the result/parameter
         * combination is being changed.
         */
        boolean combinationChanged =
                !resultValue.getLabResult()
                        .getId()
                        .equals(request.getLabResultId())
                        ||
                        !resultValue.getLabParameter()
                                .getId()
                                .equals(request.getLabParameterId());

        if (combinationChanged) {
            validateDuplicateParameter(
                    request.getLabResultId(),
                    request.getLabParameterId()
            );
        }

        resultValue.setLabResult(labResult);
        resultValue.setLabParameter(labParameter);
        resultValue.setResultValue(request.getResultValue());
        resultValue.setUnit(request.getUnit());
        resultValue.setReferenceRange(request.getReferenceRange());

        resultValue.setAbnormal(
                request.getAbnormal() != null
                        ? request.getAbnormal()
                        : false
        );

        resultValue.setComments(request.getComments());

        LabResultValue updatedResultValue =
                labResultValueRepository.save(resultValue);

        log.info(
                "Lab result value updated successfully. ID: {}",
                updatedResultValue.getId()
        );

        return mapToResponse(updatedResultValue);
    }

    @Override
    public void deleteResultValue(Long id) {

        log.info(
                "Deleting lab result value. ID: {}",
                id
        );

        LabResultValue resultValue =
                findResultValue(id);

        labResultValueRepository.delete(resultValue);

        log.info(
                "Lab result value deleted successfully. ID: {}",
                id
        );
    }

    private LabResultValue findResultValue(Long id) {

        return labResultValueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab result value not found with ID: "
                                        + id
                        )
                );
    }

    private LabResult findLabResult(Long id) {

        return labResultRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab result not found with ID: "
                                        + id
                        )
                );
    }

    private LabParameter findLabParameter(Long id) {

        return lapParameterRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab parameter not found with ID: "
                                        + id
                        )
                );
    }

    private void validateParameterBelongsToResult(
            LabResult labResult,
            LabParameter labParameter) {

        Long resultTestId =
                labResult.getLabTest().getId();

        Long parameterTestId =
                labParameter.getLabTest().getId();

        if (!resultTestId.equals(parameterTestId)) {

            throw new IllegalArgumentException(
                    "Lab parameter is not associated with "
                            + "the test of this result"
            );
        }
    }

    private void validateDuplicateParameter(
            Long labResultId,
            Long labParameterId) {

        boolean exists =
                labResultValueRepository
                        .existsByLabResultIdAndLabParameterId(
                                labResultId,
                                labParameterId
                        );

        if (exists) {

            throw new IllegalArgumentException(
                    "Lab parameter is already added "
                            + "to this lab result"
            );
        }
    }

    private LabResultValueResponseDTO mapToResponse(
            LabResultValue resultValue) {

        return LabResultValueResponseDTO.builder()
                .id(resultValue.getId())

                .labResultId(
                        resultValue.getLabResult()
                                .getId()
                )

                .resultNumber(
                        resultValue.getLabResult()
                                .getResultNumber()
                )

                .labParameterId(
                        resultValue.getLabParameter()
                                .getId()
                )

                .parameterCode(
                        resultValue.getLabParameter()
                                .getParameterCode()
                )

                .parameterName(
                        resultValue.getLabParameter()
                                .getParameterName()
                )

                .resultValue(
                        resultValue.getResultValue()
                )

                .unit(
                        resultValue.getUnit()
                )

                .referenceRange(
                        resultValue.getReferenceRange()
                )

                .abnormal(
                        resultValue.getAbnormal()
                )

                .comments(
                        resultValue.getComments()
                )

                .createdAt(
                        resultValue.getCreatedAt()
                )

                .updatedAt(
                        resultValue.getUpdatedAt()
                )

                .build();
    }
}