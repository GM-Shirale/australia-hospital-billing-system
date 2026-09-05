package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.BillItemRequest;
import com.hospital.hospital_billing_system.billing.dto.BillItemResponse;
import com.hospital.hospital_billing_system.billing.entity.Bill;
import com.hospital.hospital_billing_system.billing.entity.BillItem;
import com.hospital.hospital_billing_system.billing.repository.BillItemRepository;
import com.hospital.hospital_billing_system.billing.repository.BillRepository;
import com.hospital.hospital_billing_system.billing.service.BillItemService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BillItemServiceImpl implements BillItemService {

    private static final Logger log =
            LoggerFactory.getLogger(BillItemServiceImpl.class);

    private final BillItemRepository billItemRepository;
    private final BillRepository billRepository;

    // constructor injection
    public BillItemServiceImpl(
            BillItemRepository billItemRepository,
            BillRepository billRepository) {

        this.billItemRepository = billItemRepository;
        this.billRepository = billRepository;
    }

    @Override
    public BillItemResponse addBillItem(
            Long billId,
            BillItemRequest request) {

        log.info("Adding bill item to bill with id: {}", billId);

        // find bill
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill not found with id: " + billId
                        )
                );

        // calculate item amount
        BigDecimal amount = request.getUnitPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        // create bill item using builder
        BillItem billItem = BillItem.builder()
                .bill(bill)
                .serviceType(request.getServiceType())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .amount(amount)
                .build();

        // save bill item
        BillItem savedItem =
                billItemRepository.save(billItem);

        log.info(
                "Bill item created successfully with id: {}",
                savedItem.getBillItemId()
        );

        return mapToResponse(savedItem);
    }

    @Override
    public BillItemResponse getBillItemById(
            Long billItemId) {

        log.info(
                "Fetching bill item with id: {}",
                billItemId
        );

        BillItem billItem = billItemRepository
                .findById(billItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill item not found with id: " + billItemId
                        )
                );

        return mapToResponse(billItem);
    }

    @Override
    public List<BillItemResponse> getBillItemsByBillId(
            Long billId) {

        log.info(
                "Fetching bill items for bill with id: {}",
                billId
        );

        // check bill exists
        if (!billRepository.existsById(billId)) {
            throw new ResourceNotFoundException(
                    "Bill not found with id: " + billId
            );
        }

        return billItemRepository.findByBillBillId(billId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BillItemResponse updateBillItem(
            Long billItemId,
            BillItemRequest request) {

        log.info(
                "Updating bill item with id: {}",
                billItemId
        );

        BillItem billItem = billItemRepository
                .findById(billItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill item not found with id: " + billItemId
                        )
                );

        // calculate new item amount
        BigDecimal amount = request.getUnitPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        // update item details
        billItem.setServiceType(request.getServiceType());
        billItem.setDescription(request.getDescription());
        billItem.setQuantity(request.getQuantity());
        billItem.setUnitPrice(request.getUnitPrice());
        billItem.setAmount(amount);

        BillItem updatedItem =
                billItemRepository.save(billItem);

        log.info(
                "Bill item updated successfully with id: {}",
                billItemId
        );

        return mapToResponse(updatedItem);
    }

    @Override
    public void deleteBillItem(Long billItemId) {

        log.info(
                "Deleting bill item with id: {}",
                billItemId
        );

        if (!billItemRepository.existsById(billItemId)) {
            throw new ResourceNotFoundException(
                    "Bill item not found with id: " + billItemId
            );
        }

        billItemRepository.deleteById(billItemId);

        log.info(
                "Bill item deleted successfully with id: {}",
                billItemId
        );
    }

    // convert entity to response
    private BillItemResponse mapToResponse(
            BillItem billItem) {

        return BillItemResponse.builder()
                .billItemId(billItem.getBillItemId())
                .billId(billItem.getBill().getBillId())
                .serviceType(billItem.getServiceType())
                .description(billItem.getDescription())
                .quantity(billItem.getQuantity())
                .unitPrice(billItem.getUnitPrice())
                .amount(billItem.getAmount())
                .build();
    }
}