package com.hospital.hospital_billing_system.laboratory.service.impl;

import com.hospital.hospital_billing_system.common.enums.SampleStatus;
import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabSampleRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabSampleResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabOrder;
import com.hospital.hospital_billing_system.laboratory.entity.LabSample;
import com.hospital.hospital_billing_system.laboratory.repo.LabOrderRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabSampleRepository;
import com.hospital.hospital_billing_system.laboratory.service.LabSampleService;
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
public class LabSampleServiceImpl implements LabSampleService {
    private final LabSampleRepository labSampleRepository;
    private final LabOrderRepository labOrderRepository;


    public LabSampleResponseDTO createSample(
            LabSampleRequestDTO request) {

        log.info(
                "Creating lab sample for Lab Order ID: {}",
                request.getLabOrderId()
        );

        LabOrder labOrder = findLabOrder(request.getLabOrderId());

        if (request.getBarcode() != null
                && !request.getBarcode().isBlank()
                && labSampleRepository.existsByBarcode(request.getBarcode())) {

            throw new DuplicateResourceException(
                    "Barcode already exists: " + request.getBarcode()
            );
        }

        LabSample sample = LabSample.builder()
                .sampleNumber(generateSampleNumber())
                .labOrder(labOrder)
                .sampleType(request.getSampleType())
                .barcode(request.getBarcode())
                .collectedAt(LocalDateTime.now())
                .status(SampleStatus.COLLECTED)
                .collectionNotes(request.getCollectionNotes())
                .build();

        LabSample savedSample =
                labSampleRepository.save(sample);

        log.info(
                "Lab sample created successfully. ID: {}",
                savedSample.getId()
        );

        return mapToResponse(savedSample);
    }


    @Transactional(readOnly = true)
    public LabSampleResponseDTO getSampleById(Long id) {

        log.debug("Fetching lab sample with ID: {}", id);
        LabSample sample =
                labSampleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab sample not found with ID: " + id
                                )
                        );

        return mapToResponse(sample);
    }


    @Transactional(readOnly = true)
    public List<LabSampleResponseDTO> getAllSamples() {

        log.debug("Fetching all lab samples");

        return labSampleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LabSampleResponseDTO> getSamplesByLabOrder(
            Long labOrderId) {

        log.debug(
                "Fetching samples for Lab Order ID: {}",
                labOrderId
        );

        findLabOrder(labOrderId);

        return labSampleRepository
                .findByLabOrderId(labOrderId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    public LabSampleResponseDTO updateSample(
            Long id,
            LabSampleRequestDTO request) {

        log.info("Updating lab sample. ID: {}", id);

        LabSample sample =
                labSampleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab sample not found with ID: " + id
                                )
                        );

        LabOrder labOrder =
                findLabOrder(request.getLabOrderId());

        if (request.getBarcode() != null
                && !request.getBarcode().isBlank()
                && labSampleRepository.existsByBarcodeAndIdNot(
                request.getBarcode(),
                id)) {

            throw new DuplicateResourceException(
                    "Barcode already exists: " + request.getBarcode()
            );
        }

        sample.setLabOrder(labOrder);
        sample.setSampleType(request.getSampleType());
        sample.setBarcode(request.getBarcode());
        sample.setCollectionNotes(request.getCollectionNotes());

        LabSample updatedSample =
                labSampleRepository.save(sample);

        log.info(
                "Lab sample updated successfully. ID: {}",
                updatedSample.getId()
        );

        return mapToResponse(updatedSample);
    }


    public void deleteSample(Long id) {

        log.info("Deleting lab sample. ID: {}", id);

        LabSample sample =
                labSampleRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab sample not found with ID: " + id
                                )
                        );

        labSampleRepository.delete(sample);

        log.info(
                "Lab sample deleted successfully. ID: {}",
                id
        );
    }

    private LabOrder findLabOrder(Long labOrderId) {

        return labOrderRepository.findById(labOrderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab order not found with ID: "
                                        + labOrderId
                        )
                );
    }

    private String generateSampleNumber() {

        String prefix = "LAB-SMP-";

        String date =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern("yyyyMMdd")
                        );

        String sampleNumber;

        do {
            sampleNumber =
                    prefix
                            + date
                            + "-"
                            + String.format(
                            "%04d",
                            (int) (Math.random() * 10000)
                    );

        } while (
                labSampleRepository
                        .existsBySampleNumber(sampleNumber)
        );

        return sampleNumber;
    }

    private LabSampleResponseDTO mapToResponse(
            LabSample sample) {

        return LabSampleResponseDTO.builder()
                .id(sample.getId())
                .sampleNumber(sample.getSampleNumber())
                .labOrderId(sample.getLabOrder().getId())
                .orderNumber(sample.getLabOrder().getOrderNumber())
                .sampleType(sample.getSampleType())
                .barcode(sample.getBarcode())
                .collectedAt(sample.getCollectedAt())
                .receivedAt(sample.getReceivedAt())
                .status(sample.getStatus().name())
                .collectionNotes(sample.getCollectionNotes())
                .createdAt(sample.getCreatedAt())
                .updatedAt(sample.getUpdatedAt())
                .build();
    }
}
