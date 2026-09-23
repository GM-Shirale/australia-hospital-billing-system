package com.hospital.hospital_billing_system.billing.service;

import com.hospital.hospital_billing_system.billing.dto.BillRequest;
import com.hospital.hospital_billing_system.billing.dto.BillResponse;

import java.util.List;

public interface BillService {

    // create hospital bill
    BillResponse createBill(Long patientId, BillRequest request);

    // get bill by id
    BillResponse getBillById(Long billId);

    // get all bills
    List<BillResponse> getAllBills();

    // get all bills of a patient
    List<BillResponse> getBillsByPatientId(Long patientId);

    // update bill
    BillResponse updateBill(Long billId, BillRequest request);

    // delete bill
    void deleteBill(Long billId);
}