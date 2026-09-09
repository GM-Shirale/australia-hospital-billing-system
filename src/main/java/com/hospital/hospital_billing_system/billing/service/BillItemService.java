package com.hospital.hospital_billing_system.billing.service;

import com.hospital.hospital_billing_system.billing.dto.BillItemRequest;
import com.hospital.hospital_billing_system.billing.dto.BillItemResponse;

import java.util.List;

public interface BillItemService {

    // add item to a bill
    BillItemResponse addBillItem(Long billId, BillItemRequest request);

    // get bill item by id
    BillItemResponse getBillItemById(Long billItemId);

    // get all items of a bill
    List<BillItemResponse> getBillItemsByBillId(Long billId);

    // update bill item
    BillItemResponse updateBillItem(
            Long billItemId,
            BillItemRequest request
    );

    // delete bill item
    void deleteBillItem(Long billItemId);
}