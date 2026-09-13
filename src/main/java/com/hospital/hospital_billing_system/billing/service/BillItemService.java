package com.hospital.hospital_billing_system.billing.service;

import com.hospital.hospital_billing_system.billing.dto.BillItemRequest;
import com.hospital.hospital_billing_system.billing.dto.BillItemResponse;

import java.util.List;

public interface BillItemService {

    BillItemResponse createBillItem(Long billId, BillItemRequest request);

    BillItemResponse getBillItemById(Long billItemId);

    List<BillItemResponse> getBillItemsByBillId(Long billId);

    BillItemResponse updateBillItem(Long billItemId, BillItemRequest request);

    void deleteBillItem(Long billItemId);
}