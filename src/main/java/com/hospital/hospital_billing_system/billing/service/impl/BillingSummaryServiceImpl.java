package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.BillingSummaryResponse;
import com.hospital.hospital_billing_system.billing.entity.Bill;
import com.hospital.hospital_billing_system.billing.entity.Payment;
import com.hospital.hospital_billing_system.billing.entity.PaymentTransaction;
import com.hospital.hospital_billing_system.billing.entity.Refund;
import com.hospital.hospital_billing_system.billing.repository.BillRepository;
import com.hospital.hospital_billing_system.billing.repository.PaymentRepository;
import com.hospital.hospital_billing_system.billing.repository.PaymentTransactionRepository;
import com.hospital.hospital_billing_system.billing.repository.RefundRepository;
import com.hospital.hospital_billing_system.billing.service.BillingSummaryService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingSummaryServiceImpl implements BillingSummaryService {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final RefundRepository refundRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional(readOnly = true)
    public BillingSummaryResponse getBillingSummary() {

        log.info("Fetching hospital billing summary");

        List<Bill> bills = billRepository.findAll();

        BigDecimal totalBilledAmount = bills.stream()
                .map(Bill::getTotalAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPatientAmount = bills.stream()
                .map(Bill::getPatientAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaidAmount = paymentRepository.findAll()
                .stream()
                .map(Payment::getPaidAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRemainingAmount = totalPatientAmount
                .subtract(totalPaidAmount);

        BigDecimal totalRefundedAmount = refundRepository.findAll()
                .stream()
                .map(Refund::getRefundAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return BillingSummaryResponse.builder()
                .totalBills((long) bills.size())
                .totalBilledAmount(totalBilledAmount)
                .totalPatientAmount(totalPatientAmount)
                .totalPaidAmount(totalPaidAmount)
                .totalRemainingAmount(totalRemainingAmount)
                .totalRefundedAmount(totalRefundedAmount)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BillingSummaryResponse getBillingSummary(Long patientId) {

        log.info("Fetching billing summary for patient id: {}", patientId);

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId);
        }

        List<Bill> bills = billRepository.findByPatientPatientId(patientId);

        BigDecimal totalBilledAmount = bills.stream()
                .map(Bill::getTotalAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPatientAmount = bills.stream()
                .map(Bill::getPatientAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaidAmount = BigDecimal.ZERO;
        BigDecimal totalRefundedAmount = BigDecimal.ZERO;

        for (Bill bill : bills) {

            Payment payment = paymentRepository
                    .findByBillBillId(bill.getBillId())
                    .orElse(null);

            if (payment == null) {
                continue;
            }

            if (payment.getPaidAmount() != null) {
                totalPaidAmount = totalPaidAmount
                        .add(payment.getPaidAmount());
            }

            List<PaymentTransaction> transactions =
                    paymentTransactionRepository
                            .findByPaymentPaymentId(payment.getPaymentId());

            for (PaymentTransaction transaction : transactions) {

                List<Refund> refunds =
                        refundRepository
                                .findByPaymentTransactionTransactionId(
                                        transaction.getTransactionId());

                for (Refund refund : refunds) {

                    if (refund.getRefundAmount() != null) {
                        totalRefundedAmount = totalRefundedAmount
                                .add(refund.getRefundAmount());
                    }
                }
            }
        }

        BigDecimal totalRemainingAmount = totalPatientAmount
                .subtract(totalPaidAmount);

        return BillingSummaryResponse.builder()
                .totalBills((long) bills.size())
                .totalBilledAmount(totalBilledAmount)
                .totalPatientAmount(totalPatientAmount)
                .totalPaidAmount(totalPaidAmount)
                .totalRemainingAmount(totalRemainingAmount)
                .totalRefundedAmount(totalRefundedAmount)
                .build();
    }
}