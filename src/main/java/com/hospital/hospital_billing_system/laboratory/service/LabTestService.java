package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabTestRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabTestResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabTest;
import com.hospital.hospital_billing_system.laboratory.repo.LabTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LabTestService {

    private final LabTestRepository labTestRepository;

    public LabTestResponseDTO createLabTest(LabTestRequestDTO request) {

        log.info(
                "Creating lab test. Test code: {}, Test name: {}",
                request.getTestCode(),
                request.getTestName()
        );

        if (labTestRepository.existsByTestCode(request.getTestCode())) {

            log.warn(
                    "Duplicate lab test code detected: {}",
                    request.getTestCode()
            );

            throw new DuplicateResourceException(
                    "Lab test already exists with code: "
                            + request.getTestCode()
            );
        }

        LabTest labTest = LabTest.builder()
                .testCode(request.getTestCode())
                .testName(request.getTestName())
                .category(request.getCategory())
                .sampleType(request.getSampleType())
                .description(request.getDescription())
                .price(request.getPrice())
                .turnaroundTime(request.getTurnaroundTime())
                .active(request.getActive() != null
                        ? request.getActive()
                        : true)
                .build();

        LabTest savedLabTest = labTestRepository.save(labTest);

        log.info(
                "Lab test created successfully. ID: {}, Test code: {}",
                savedLabTest.getId(),
                savedLabTest.getTestCode()
        );

        return mapToResponse(savedLabTest);
    }

    @Transactional(readOnly = true)
    public LabTestResponseDTO getLabTestById(Long id) {

        log.debug("Fetching lab test with ID: {}", id);

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Lab test not found with ID: {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Lab test not found with ID: " + id
                    );
                });

        log.debug(
                "Lab test found. ID: {}, Test code: {}",
                labTest.getId(),
                labTest.getTestCode()
        );

        return mapToResponse(labTest);
    }

    @Transactional(readOnly = true)
    public List<LabTestResponseDTO> getAllLabTests() {

        log.debug("Fetching all lab tests");

        List<LabTestResponseDTO> labTests =
                labTestRepository.findAll()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        log.info(
                "Successfully fetched {} lab tests",
                labTests.size()
        );

        return labTests;
    }

    public LabTestResponseDTO updateLabTest(
            Long id,
            LabTestRequestDTO request) {

        log.info(
                "Updating lab test. ID: {}, New test code: {}",
                id,
                request.getTestCode()
        );

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Cannot update lab test. Lab test not found with ID: {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Lab test not found with ID: " + id
                    );
                });

        if (!labTest.getTestCode().equals(request.getTestCode())
                && labTestRepository.existsByTestCode(
                request.getTestCode())) {

            log.warn(
                    "Duplicate lab test code detected during update. Code: {}",
                    request.getTestCode()
            );

            throw new DuplicateResourceException(
                    "Lab test already exists with code: "
                            + request.getTestCode()
            );
        }

        labTest.setTestCode(request.getTestCode());
        labTest.setTestName(request.getTestName());
        labTest.setCategory(request.getCategory());
        labTest.setSampleType(request.getSampleType());
        labTest.setDescription(request.getDescription());
        labTest.setPrice(request.getPrice());
        labTest.setTurnaroundTime(request.getTurnaroundTime());

        if (request.getActive() != null) {
            labTest.setActive(request.getActive());
        }

        LabTest updatedLabTest =
                labTestRepository.save(labTest);

        log.info(
                "Lab test updated successfully. ID: {}, Test code: {}",
                updatedLabTest.getId(),
                updatedLabTest.getTestCode()
        );

        return mapToResponse(updatedLabTest);
    }

    public void deleteLabTest(Long id) {

        log.info("Deleting lab test. ID: {}", id);

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Cannot delete lab test. Lab test not found with ID: {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Lab test not found with ID: " + id
                    );
                });

        labTestRepository.delete(labTest);

        log.info(
                "Lab test deleted successfully. ID: {}, Test code: {}",
                id,
                labTest.getTestCode()
        );
    }

    private LabTestResponseDTO mapToResponse(LabTest labTest) {

        return LabTestResponseDTO.builder()
                .id(labTest.getId())
                .testCode(labTest.getTestCode())
                .testName(labTest.getTestName())
                .category(labTest.getCategory())
                .sampleType(labTest.getSampleType())
                .description(labTest.getDescription())
                .price(labTest.getPrice())
                .turnaroundTime(labTest.getTurnaroundTime())
                .active(labTest.getActive())
                .createdAt(labTest.getCreatedAt())
                .updatedAt(labTest.getUpdatedAt())
                .build();
    }
}