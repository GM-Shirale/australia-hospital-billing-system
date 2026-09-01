package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.laboratory.dto.LabTestRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabTestResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabTest;
import com.hospital.hospital_billing_system.laboratory.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.laboratory.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.repo.LabTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LabTestService {

    private final LabTestRepository labTestRepository;

    public LabTestResponseDTO createLabTest(LabTestRequestDTO request) {

        if (labTestRepository.existsByTestCode(request.getTestCode())) {
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

        return mapToResponse(savedLabTest);
    }

    @Transactional(readOnly = true)
    public LabTestResponseDTO getLabTestById(Long id) {

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab test not found with ID: " + id
                        ));

        return mapToResponse(labTest);
    }

    @Transactional(readOnly = true)
    public List<LabTestResponseDTO> getAllLabTests() {

        return labTestRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public LabTestResponseDTO updateLabTest(
            Long id,
            LabTestRequestDTO request) {

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab test not found with ID: " + id
                        ));

        if (!labTest.getTestCode().equals(request.getTestCode())
                && labTestRepository.existsByTestCode(request.getTestCode())) {

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

        LabTest updatedLabTest = labTestRepository.save(labTest);

        return mapToResponse(updatedLabTest);
    }

    public void deleteLabTest(Long id) {

        LabTest labTest = labTestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab test not found with ID: " + id
                        ));

        labTestRepository.delete(labTest);
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
