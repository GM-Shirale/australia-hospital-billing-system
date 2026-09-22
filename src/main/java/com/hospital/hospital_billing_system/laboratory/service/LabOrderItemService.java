package com.hospital.hospital_billing_system.laboratory.service;

import com.hospital.hospital_billing_system.laboratory.dto.LabOrderItemRequestDTO;
import com.hospital.hospital_billing_system.laboratory.dto.LabOrderItemResponseDTO;

import java.util.List;

public interface LabOrderItemService {

    LabOrderItemResponseDTO createOrderItem(
            LabOrderItemRequestDTO request
    );

    LabOrderItemResponseDTO getOrderItemById(Long id);

    List<LabOrderItemResponseDTO> getAllOrderItems();

    List<LabOrderItemResponseDTO> getOrderItemsByLabOrder(
            Long labOrderId
    );

    LabOrderItemResponseDTO updateOrderItem(
            Long id,
            LabOrderItemRequestDTO request
    );

    void deleteOrderItem(Long id);

}
