package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.BillItemRequest;
import com.hospital.hospital_billing_system.billing.dto.BillItemResponse;
import com.hospital.hospital_billing_system.billing.entity.Bill;
import com.hospital.hospital_billing_system.billing.entity.BillItem;
import com.hospital.hospital_billing_system.billing.entity.BillItemType;
import com.hospital.hospital_billing_system.billing.repository.BillItemRepository;
import com.hospital.hospital_billing_system.billing.repository.BillRepository;
import com.hospital.hospital_billing_system.billing.service.BillItemService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                        new ResourceNotFoundException(
                                "Bill not found with id: " + billId));

        validateRequest(request);

        // Check whether the logged-in user can add this charge type
        validateItemTypeAccess(request.getItemType());

        BigDecimal totalAmount = request.getUnitPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        // Add bill item amount to the bill total
        bill.setTotalAmount(
                bill.getTotalAmount().add(totalAmount)
        );

        billRepository.save(bill);

        BillItem billItem = BillItem.builder()
                .bill(bill)
                .itemName(request.getItemName())
                .itemType(request.getItemType())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .totalAmount(totalAmount)
                .build();

        BillItem savedBillItem = billItemRepository.save(billItem);

        log.info("Bill item created successfully with id: {}",
                savedBillItem.getBillItemId());

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

        // Check whether the logged-in user can view this charge type
        validateItemTypeViewAccess(billItem.getItemType());

        return mapToResponse(billItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillItemResponse> getBillItemsByBillId(Long billId) {

        log.info("Fetching bill items for bill id: {}", billId);

        if (!billRepository.existsById(billId)) {
            throw new ResourceNotFoundException(
                    "Bill not found with id: " + billId);
        }

        // Return only the item types the current user can view
        return billItemRepository.findByBillBillId(billId)
                .stream()
                .filter(billItem ->
                        canViewItemType(billItem.getItemType()))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public BillItemResponse updateBillItem(
            Long billItemId,
            BillItemRequest request) {

        log.info("Updating bill item with id: {}", billItemId);

        BillItem billItem = billItemRepository.findById(billItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill item not found with id: " + billItemId));

        validateRequest(request);

        // Check whether the logged-in user can update the new charge type
        validateItemTypeAccess(request.getItemType());

        // Check whether the user can modify the existing charge
        validateItemTypeAccess(billItem.getItemType());

        BigDecimal oldTotalAmount = billItem.getTotalAmount();

        BigDecimal newTotalAmount = request.getUnitPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        Bill bill = billItem.getBill();

        // Remove old amount and add new amount
        bill.setTotalAmount(
                bill.getTotalAmount()
                        .subtract(oldTotalAmount)
                        .add(newTotalAmount)
        );

        billRepository.save(bill);

        billItem.setItemName(request.getItemName());
        billItem.setItemType(request.getItemType());
        billItem.setQuantity(request.getQuantity());
        billItem.setUnitPrice(request.getUnitPrice());
        billItem.setTotalAmount(newTotalAmount);

        BillItem updatedBillItem = billItemRepository.save(billItem);

        log.info("Bill item updated successfully with id: {}", billItemId);

        return mapToResponse(updatedBillItem);
    }

    @Override
    @Transactional
    public void deleteBillItem(Long billItemId) {

        log.info("Deleting bill item with id: {}", billItemId);

        BillItem billItem = billItemRepository.findById(billItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill item not found with id: " + billItemId));

        // Check whether the user can delete this charge type
        validateItemTypeAccess(billItem.getItemType());

        Bill bill = billItem.getBill();

        // Remove deleted item amount from the bill total
        bill.setTotalAmount(
                bill.getTotalAmount()
                        .subtract(billItem.getTotalAmount())
        );

        billRepository.save(bill);

        billItemRepository.delete(billItem);

        log.info("Bill item deleted successfully with id: {}", billItemId);
    }

    private void validateRequest(BillItemRequest request) {

        if (request == null) {
            throw new IllegalStateException(
                    "Bill item request is required");
        }

        if (request.getItemName() == null ||
                request.getItemName().isBlank()) {
            throw new IllegalStateException(
                    "Item name is required");
        }

        if (request.getItemType() == null) {
            throw new IllegalStateException(
                    "Item type is required");
        }

        if (request.getQuantity() == null ||
                request.getQuantity() <= 0) {
            throw new IllegalStateException(
                    "Quantity must be greater than zero");
        }

        if (request.getUnitPrice() == null ||
                request.getUnitPrice()
                        .compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(
                    "Unit price cannot be negative");
        }
    }

    private void validateItemTypeAccess(BillItemType itemType) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new AccessDeniedException(
                    "User is not authenticated");
        }

        // Admin can access all bill item types
        if (hasRole("ROLE_ADMIN")) {
            return;
        }

        switch (itemType) {

            case MEDICAL -> {
                if (!hasRole("ROLE_DOCTOR")) {
                    throw new AccessDeniedException(
                            "Only DOCTOR can add or modify medical charges");
                }
            }

            case LAB -> {
                if (!hasRole("ROLE_LAB_STAFF")) {
                    throw new AccessDeniedException(
                            "Only LAB_STAFF can add or modify lab charges");
                }
            }

            case PHARMACY -> {
                if (!hasRole("ROLE_PHARMACY_STAFF")) {
                    throw new AccessDeniedException(
                            "Only PHARMACY_STAFF can add or modify pharmacy charges");
                }
            }

            case ROOM -> {
                if (!hasRole("ROLE_BILLING_STAFF")) {
                    throw new AccessDeniedException(
                            "Only BILLING_STAFF can add or modify room charges");
                }
            }
        }
    }

    private void validateItemTypeViewAccess(
            BillItemType itemType) {

        if (!canViewItemType(itemType)) {
            throw new AccessDeniedException(
                    "You are not authorized to view this bill item");
        }
    }

    private boolean canViewItemType(
            BillItemType itemType) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return false;
        }

        // Admin can view everything
        if (hasRole("ROLE_ADMIN")) {
            return true;
        }

        // Billing staff can view all bill items
        if (hasRole("ROLE_BILLING_STAFF")) {
            return true;
        }

        return switch (itemType) {
            case MEDICAL -> hasRole("ROLE_DOCTOR");
            case LAB -> hasRole("ROLE_LAB_STAFF");
            case PHARMACY -> hasRole("ROLE_PHARMACY_STAFF");
            case ROOM -> hasRole("ROLE_BILLING_STAFF");
        };
    }

    private boolean hasRole(String role) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals(role));
    }

    private BillItemResponse mapToResponse(
            BillItem billItem) {

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