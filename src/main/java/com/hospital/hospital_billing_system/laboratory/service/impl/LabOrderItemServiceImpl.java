package com.hospital.hospital_billing_system.laboratory.service.impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.laboratory.dto.LabOrderItemRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabOrderItemResponseDTO;
import com.hospital.hospital_billing_system.laboratory.entity.LabOrder;
import com.hospital.hospital_billing_system.laboratory.entity.LabOrderItem;
import com.hospital.hospital_billing_system.laboratory.entity.LabTest;
import com.hospital.hospital_billing_system.laboratory.repo.LabOrderItemRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabOrderRepository;
import com.hospital.hospital_billing_system.laboratory.repo.LabTestRepository;
import com.hospital.hospital_billing_system.laboratory.service.LabOrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LabOrderItemServiceImpl implements LabOrderItemService {

    private final LabOrderItemRepository labOrderItemRepository;
    private final LabOrderRepository labOrderRepository;
    private final LabTestRepository labTestRepository;

    @Override
    public LabOrderItemResponseDTO createOrderItem(
            LabOrderItemRequestDTO request) {

        log.info(
                "Creating lab order item. Lab Order ID: {}, Lab Test ID: {}",
                request.getLabOrderId(),
                request.getLabTestId()
        );

        LabOrder labOrder = findLabOrder(request.getLabOrderId());

        LabTest labTest = findLabTest(request.getLabTestId());

        if (labOrderItemRepository.existsByLabOrderIdAndLabTestId(
                request.getLabOrderId(),
                request.getLabTestId())) {

            throw new DuplicateResourceException(
                    "Lab test already exists in this lab order"
            );
        }

        LabOrderItem orderItem = LabOrderItem.builder()
                .labOrder(labOrder)
                .labTest(labTest)
                .quantity(request.getQuantity())
                .price(labTest.getPrice())
                .clinicalNotes(request.getClinicalNotes())
                .build();

        LabOrderItem savedItem =
                labOrderItemRepository.save(orderItem);

        log.info(
                "Lab order item created successfully. ID: {}",
                savedItem.getId()
        );

        return mapToResponse(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public LabOrderItemResponseDTO getOrderItemById(Long id) {

        log.debug("Fetching lab order item with ID: {}", id);

        LabOrderItem orderItem =
                labOrderItemRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab order item not found with ID: " + id
                                )
                        );

        return mapToResponse(orderItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderItemResponseDTO> getAllOrderItems() {

        log.debug("Fetching all lab order items");

        return labOrderItemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabOrderItemResponseDTO> getOrderItemsByLabOrder(
            Long labOrderId) {

        log.debug(
                "Fetching lab order items for Lab Order ID: {}",
                labOrderId
        );

        // Verify that LabOrder exists
        findLabOrder(labOrderId);

        return labOrderItemRepository
                .findByLabOrderId(labOrderId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public LabOrderItemResponseDTO updateOrderItem(
            Long id,
            LabOrderItemRequestDTO request) {

        log.info("Updating lab order item. ID: {}", id);

        LabOrderItem orderItem =
                labOrderItemRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab order item not found with ID: " + id
                                )
                        );

        LabOrder labOrder =
                findLabOrder(request.getLabOrderId());

        LabTest labTest =
                findLabTest(request.getLabTestId());

        boolean orderChanged =
                !orderItem.getLabOrder()
                        .getId()
                        .equals(request.getLabOrderId());

        boolean testChanged =
                !orderItem.getLabTest()
                        .getId()
                        .equals(request.getLabTestId());

        if ((orderChanged || testChanged)
                && labOrderItemRepository
                .existsByLabOrderIdAndLabTestId(
                        request.getLabOrderId(),
                        request.getLabTestId())) {

            throw new DuplicateResourceException(
                    "Lab test already exists in this lab order"
            );
        }

        orderItem.setLabOrder(labOrder);
        orderItem.setLabTest(labTest);
        orderItem.setQuantity(request.getQuantity());

        // Always take current price from LabTest
        orderItem.setPrice(labTest.getPrice());

        orderItem.setClinicalNotes(request.getClinicalNotes());

        LabOrderItem updatedItem =
                labOrderItemRepository.save(orderItem);

        log.info(
                "Lab order item updated successfully. ID: {}",
                updatedItem.getId()
        );

        return mapToResponse(updatedItem);
    }

    @Override
    public void deleteOrderItem(Long id) {

        log.info("Deleting lab order item. ID: {}", id);

        LabOrderItem orderItem =
                labOrderItemRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Lab order item not found with ID: " + id
                                )
                        );

        labOrderItemRepository.delete(orderItem);

        log.info(
                "Lab order item deleted successfully. ID: {}",
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

    private LabTest findLabTest(Long labTestId) {

        return labTestRepository.findById(labTestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Lab test not found with ID: "
                                        + labTestId
                        )
                );
    }

    private LabOrderItemResponseDTO mapToResponse(
            LabOrderItem orderItem) {

        return LabOrderItemResponseDTO.builder()
                .id(orderItem.getId())

                .labOrderId(
                        orderItem.getLabOrder().getId()
                )

                .orderNumber(
                        orderItem.getLabOrder().getOrderNumber()
                )

                .labTestId(
                        orderItem.getLabTest().getId()
                )

                .testCode(
                        orderItem.getLabTest().getTestCode()
                )

                .testName(
                        orderItem.getLabTest().getTestName()
                )

                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .status(orderItem.getStatus())
                .clinicalNotes(orderItem.getClinicalNotes())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }
}