package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.BillItemRequest;
import com.hospital.hospital_billing_system.billing.dto.BillItemResponse;
import com.hospital.hospital_billing_system.billing.entity.Bill;
import com.hospital.hospital_billing_system.billing.entity.BillItem;
import com.hospital.hospital_billing_system.billing.repository.BillItemRepository;
import com.hospital.hospital_billing_system.billing.repository.BillRepository;
import com.hospital.hospital_billing_system.billing.service.BillItemService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillItemServiceImpl implements BillItemService {

    private final BillItemRepository billItemRepository;
    private final BillRepository billRepository;

    @Override
    @Transactional
    public BillItemResponse createBillItem(Long billId, BillItemRequest request) {

        log.info("Creating bill item for bill id: {}", billId);

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bill not found with id: " + billId));

        validateRequest(request);

        BigDecimal totalAmount = request.getUnitPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        BillItem billItem = BillItem.builder()
                .bill(bill)
                .itemName(request.getItemName())
                .itemType(request.getItemType())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .totalAmount(totalAmount)
                .build();

        BillItem savedBillItem = billItemRepository.save(billItem);

        log.info("Bill item created successfully with id: {}", savedBillItem.getBillItemId());

        return mapToResponse(savedBillItem);
    }

    @Override
    @Transactional(readOnly = true)
    public BillItemResponse getBillItemById(Long billItemId) {

        log.info("Fetching bill item with id: {}", billItemId);

        BillItem billItem = billItemRepository.findById(billItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill item not found with id: " + billItemId));

        return mapToResponse(billItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillItemResponse> getBillItemsByBillId(Long billId) {

        log.info("Fetching bill items for bill id: {}", billId);

        if (!billRepository.existsById(billId)) {
            throw new ResourceNotFoundException("Bill not found with id: " + billId);
        }

        return billItemRepository.findByBillBillId(billId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public BillItemResponse updateBillItem(Long billItemId, BillItemRequest request) {

        log.info("Updating bill item with id: {}", billItemId);

        BillItem billItem = billItemRepository.findById(billItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill item not found with id: " + billItemId));

        validateRequest(request);

        BigDecimal totalAmount = request.getUnitPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        billItem.setItemName(request.getItemName());
        billItem.setItemType(request.getItemType());
        billItem.setQuantity(request.getQuantity());
        billItem.setUnitPrice(request.getUnitPrice());
        billItem.setTotalAmount(totalAmount);

        BillItem updatedBillItem = billItemRepository.save(billItem);

        log.info("Bill item updated successfully with id: {}", billItemId);

        return mapToResponse(updatedBillItem);
    }

    @Override
    @Transactional
    public void deleteBillItem(Long billItemId) {

        log.info("Deleting bill item with id: {}", billItemId);

        if (!billItemRepository.existsById(billItemId)) {
            throw new ResourceNotFoundException(
                    "Bill item not found with id: " + billItemId);
        }

        billItemRepository.deleteById(billItemId);

        log.info("Bill item deleted successfully with id: {}", billItemId);
    }

    private void validateRequest(BillItemRequest request) {

        if (request.getItemName() == null || request.getItemName().isBlank()) {
            throw new IllegalStateException("Item name is required");
        }

        if (request.getItemType() == null || request.getItemType().isBlank()) {
            throw new IllegalStateException("Item type is required");
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalStateException("Quantity must be greater than zero");
        }

        if (request.getUnitPrice() == null ||
                request.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Unit price cannot be negative");
        }
    }

    private BillItemResponse mapToResponse(BillItem billItem) {

        return BillItemResponse.builder()
                .billItemId(billItem.getBillItemId())
                .billId(billItem.getBill().getBillId())
                .itemName(billItem.getItemName())
                .itemType(billItem.getItemType())
                .quantity(billItem.getQuantity())
                .unitPrice(billItem.getUnitPrice())
                .totalAmount(billItem.getTotalAmount())
                .build();
    }
}