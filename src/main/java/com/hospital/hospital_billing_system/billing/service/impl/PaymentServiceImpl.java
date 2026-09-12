package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.PaymentRequest;
import com.hospital.hospital_billing_system.billing.dto.PaymentResponse;
import com.hospital.hospital_billing_system.billing.entity.Bill;
import com.hospital.hospital_billing_system.billing.entity.Payment;
import com.hospital.hospital_billing_system.billing.entity.PaymentStatus;
import com.hospital.hospital_billing_system.billing.repository.BillRepository;
import com.hospital.hospital_billing_system.billing.repository.PaymentRepository;
import com.hospital.hospital_billing_system.billing.service.PaymentService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {

        log.info("Creating payment for bill id: {}", request.getBillId());

        // find bill
        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bill not found with id: " + request.getBillId()
                        )
                );

        // validate payment amount
        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalStateException(
                    "Payment amount must be greater than zero"
            );
        }

        // patient responsibility
        BigDecimal patientAmount = bill.getPatientAmount();

        // find existing payment for this bill
        Payment payment = paymentRepository
                .findByBillBillId(bill.getBillId())
                .orElse(null);

        // create payment if it does not exist
        if (payment == null) {

            payment = Payment.builder()
                    .paymentNumber(generatePaymentNumber())
                    .bill(bill)
                    .totalAmount(patientAmount)
                    .paidAmount(BigDecimal.ZERO)
                    .remainingAmount(patientAmount)
                    .paymentStatus(PaymentStatus.PENDING)
                    .paymentDate(LocalDateTime.now())
                    .build();
        }

        // calculate new paid amount
        BigDecimal newPaidAmount = payment.getPaidAmount()
                .add(request.getAmount());

        // prevent overpayment
        if (newPaidAmount.compareTo(patientAmount) > 0) {

            throw new IllegalStateException(
                    "Payment amount exceeds patient responsibility"
            );
        }

        BigDecimal remainingAmount = patientAmount
                .subtract(newPaidAmount);

        // update payment
        payment.setPaidAmount(newPaidAmount);
        payment.setRemainingAmount(remainingAmount);

        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            payment.setPaymentStatus(PaymentStatus.PAID);
        } else {
            payment.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        }

        Payment savedPayment = paymentRepository.save(payment);

        log.info(
                "Payment created successfully with id: {}",
                savedPayment.getPaymentId()
        );

        return mapToResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long paymentId) {

        log.info("Fetching payment with id: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + paymentId
                        )
                );

        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByBillId(Long billId) {

        log.info("Fetching payment for bill id: {}", billId);

        Payment payment = paymentRepository
                .findByBillBillId(billId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for bill id: " + billId
                        )
                );

        return mapToResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {

        log.info("Fetching all payments");

        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private String generatePaymentNumber() {

        long number = paymentRepository.count() + 1;

        return String.format(
                "PAY-%05d",
                number
        );
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .paymentNumber(payment.getPaymentNumber())
                .billId(payment.getBill().getBillId())
                .totalAmount(payment.getTotalAmount())
                .paidAmount(payment.getPaidAmount())
                .remainingAmount(payment.getRemainingAmount())
                .paymentStatus(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}