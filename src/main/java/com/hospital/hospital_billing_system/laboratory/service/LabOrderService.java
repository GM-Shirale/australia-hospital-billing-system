package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;

import com.hospital.hospital_billing_system.laboratory.dto.LabOrderRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabOrderResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabOrder;
import com.hospital.hospital_billing_system.laboratory.repo.LabOrderRepository;

import com.hospital.hospital_billing_system.patient.entity.Patient;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LabOrderService {

    private final LabOrderRepository labOrderRepository;
    private final PatientRepository patientRepository;

    // Create Lab Order
    public LabOrderResponseDTO createLabOrder(
            LabOrderRequestDTO request) {

        log.info(
                "Creating lab order. Order number: {}, Patient ID: {}",
                request.getOrderNumber(),
                request.getPatientId()
        );

        if (labOrderRepository.existsByOrderNumber(
                request.getOrderNumber())) {

            throw new DuplicateResourceException(
                    "Lab order already exists with order number: "
                            + request.getOrderNumber()
            );
        }

        Patient patient = findPatient(request.getPatientId());

        LabOrder labOrder = LabOrder.builder()
                .orderNumber(request.getOrderNumber())
                .patient(patient)
                .doctorId(null)
                .clinicalNotes(request.getClinicalNotes())
                .build();

        LabOrder savedOrder =
                labOrderRepository.save(labOrder);

        log.info(
                "Lab order created successfully. ID: {}, Order number: {}",
                savedOrder.getId(),
                savedOrder.getOrderNumber()
        );

        return mapToResponse(savedOrder);
    }

    // Get Lab Order by ID
    @Transactional(readOnly = true)
    public LabOrderResponseDTO getLabOrderById(Long id) {

        log.debug("Fetching lab order with ID: {}", id);

        LabOrder labOrder = labOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab order not found with ID: " + id
                        )
                );

        return mapToResponse(labOrder);
    }

    // Get All Lab Orders
    @Transactional(readOnly = true)
    public List<LabOrderResponseDTO> getAllLabOrders() {

        log.debug("Fetching all lab orders");

        return labOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Update Lab Order
    public LabOrderResponseDTO updateLabOrder(
            Long id,
            LabOrderRequestDTO request) {

        log.info("Updating lab order. ID: {}", id);

        LabOrder labOrder = labOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab order not found with ID: " + id
                        )
                );

        if (!labOrder.getOrderNumber()
                .equals(request.getOrderNumber())
                && labOrderRepository.existsByOrderNumberAndIdNot(
                request.getOrderNumber(),
                id)) {

            throw new DuplicateResourceException(
                    "Lab order already exists with order number: "
                            + request.getOrderNumber()
            );
        }

        Patient patient = findPatient(request.getPatientId());

        labOrder.setOrderNumber(request.getOrderNumber());
        labOrder.setPatient(patient);
        labOrder.setClinicalNotes(request.getClinicalNotes());

        LabOrder updatedOrder =
                labOrderRepository.save(labOrder);

        log.info(
                "Lab order updated successfully. ID: {}",
                updatedOrder.getId()
        );

        return mapToResponse(updatedOrder);
    }

    // Delete Lab Order
    public void deleteLabOrder(Long id) {

        log.info("Deleting lab order. ID: {}", id);

        LabOrder labOrder = labOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab order not found with ID: " + id
                        )
                );

        labOrderRepository.delete(labOrder);

        log.info(
                "Lab order deleted successfully. ID: {}",
                id
        );
    }

    // Find Patient
    private Patient findPatient(Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with ID: "
                                        + patientId
                        )
                );
    }

    // Map Entity -> Response DTO
    private LabOrderResponseDTO mapToResponse(
            LabOrder labOrder) {

        return LabOrderResponseDTO.builder()
                .id(labOrder.getId())
                .orderNumber(labOrder.getOrderNumber())
                .patientId(labOrder.getPatient().getPatientId())
                .patientNumber(labOrder.getPatient().getPatientNumber())
                .orderedAt(labOrder.getOrderDate())
                .status(labOrder.getStatus())
                .clinicalNotes(labOrder.getClinicalNotes())
                .createdAt(labOrder.getCreatedAt())
                .updatedAt(labOrder.getUpdatedAt())
                .build();
    }


}