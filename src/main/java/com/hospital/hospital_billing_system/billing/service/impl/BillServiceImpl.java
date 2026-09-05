package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.BillRequest;
import com.hospital.hospital_billing_system.billing.dto.BillResponse;
import com.hospital.hospital_billing_system.billing.entity.Bill;
import com.hospital.hospital_billing_system.billing.repository.BillRepository;
import com.hospital.hospital_billing_system.billing.service.BillService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.patient.entity.Patient;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger log =
            LoggerFactory.getLogger(BillServiceImpl.class);

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;

    // constructor injection
    public BillServiceImpl(
            BillRepository billRepository,
            PatientRepository patientRepository) {

        this.billRepository = billRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public BillResponse createBill(
            Long patientId,
            BillRequest request) {

        log.info("Creating bill for patient with id: {}", patientId);

        // find patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId
                        )
                );

        // generate bill number
        long number = billRepository.getNextBillNumber();

        String billNumber = String.format(
                "BILL-%05d",
                number
        );

        // create bill using builder
        Bill bill = Bill.builder()
                .billNumber(billNumber)
                .patient(patient)
                .totalAmount(request.getTotalAmount())
                .insuranceAmount(request.getInsuranceAmount())
                .patientAmount(request.getPatientAmount())
                .billDate(LocalDateTime.now())
                .build();

        // save bill
        Bill savedBill = billRepository.save(bill);

        log.info(
                "Bill created successfully with id: {}",
                savedBill.getBillId()
        );

        return mapToResponse(savedBill);
    }

    @Override
    public BillResponse getBillById(Long billId) {

        log.info("Fetching bill with id: {}", billId);

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill not found with id: " + billId
                        )
                );

        return mapToResponse(bill);
    }

    @Override
    public List<BillResponse> getAllBills() {

        log.info("Fetching all bills");

        return billRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<BillResponse> getBillsByPatientId(
            Long patientId) {

        log.info(
                "Fetching bills for patient with id: {}",
                patientId
        );

        // check patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId
            );
        }

        return billRepository.findByPatientPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public BillResponse updateBill(
            Long billId,
            BillRequest request) {

        log.info("Updating bill with id: {}", billId);

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill not found with id: " + billId
                        )
                );

        // update bill amounts
        bill.setTotalAmount(request.getTotalAmount());
        bill.setInsuranceAmount(request.getInsuranceAmount());
        bill.setPatientAmount(request.getPatientAmount());

        Bill updatedBill = billRepository.save(bill);

        log.info(
                "Bill updated successfully with id: {}",
                billId
        );

        return mapToResponse(updatedBill);
    }

    @Override
    public void deleteBill(Long billId) {

        log.info("Deleting bill with id: {}", billId);

        if (!billRepository.existsById(billId)) {
            throw new ResourceNotFoundException(
                    "Bill not found with id: " + billId
            );
        }

        billRepository.deleteById(billId);

        log.info(
                "Bill deleted successfully with id: {}",
                billId
        );
    }

    // convert entity to response
    private BillResponse mapToResponse(Bill bill) {

        return BillResponse.builder()
                .billId(bill.getBillId())
                .billNumber(bill.getBillNumber())
                .patientId(bill.getPatient().getPatientId())
                .totalAmount(bill.getTotalAmount())
                .insuranceAmount(bill.getInsuranceAmount())
                .patientAmount(bill.getPatientAmount())
                .billDate(bill.getBillDate())
                .build();
    }
}