package com.hospital.hospital_billing_system.billing.service.impl;

import com.hospital.hospital_billing_system.billing.dto.RefundRequest;
import com.hospital.hospital_billing_system.billing.dto.RefundResponse;
import com.hospital.hospital_billing_system.billing.entity.Payment;
import com.hospital.hospital_billing_system.billing.entity.PaymentStatus;
import com.hospital.hospital_billing_system.billing.entity.PaymentTransaction;
import com.hospital.hospital_billing_system.billing.entity.Refund;
import com.hospital.hospital_billing_system.billing.entity.RefundStatus;
import com.hospital.hospital_billing_system.billing.repository.PaymentRepository;
import com.hospital.hospital_billing_system.billing.repository.PaymentTransactionRepository;
import com.hospital.hospital_billing_system.billing.repository.RefundRepository;
import com.hospital.hospital_billing_system.billing.service.RefundService;
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
public class RefundServiceImpl implements RefundService {

    private final RefundRepository refundRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public RefundResponse createRefund(RefundRequest request) {

        log.info("Creating refund for transaction id: {}", request.getTransactionId());

        PaymentTransaction transaction = paymentTransactionRepository
                .findById(request.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment transaction not found with id: " + request.getTransactionId()));

        if (request.getRefundAmount() == null ||
                request.getRefundAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Refund amount must be greater than zero");
        }

        if (request.getRefundAmount().compareTo(transaction.getAmount()) > 0) {
            throw new IllegalStateException(
                    "Refund amount cannot exceed transaction amount");
        }

        BigDecimal previousRefundAmount = refundRepository
                .findByPaymentTransactionTransactionId(transaction.getTransactionId())
                .stream()
                .map(Refund::getRefundAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRefundAmount = previousRefundAmount
                .add(request.getRefundAmount());

        if (totalRefundAmount.compareTo(transaction.getAmount()) > 0) {
            throw new IllegalStateException(
                    "Total refund amount cannot exceed transaction amount");
        }

        RefundStatus status;

        if (totalRefundAmount.compareTo(transaction.getAmount()) == 0) {
            status = RefundStatus.SUCCESS;
        } else {
            status = RefundStatus.PARTIALLY_REFUNDED;
        }

        // Update the related payment balance
        Payment payment = transaction.getPayment();

        BigDecimal newPaidAmount = payment.getPaidAmount()
                .subtract(request.getRefundAmount());

        if (newPaidAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(
                    "Refund amount cannot exceed total paid amount");
        }

        BigDecimal newRemainingAmount = payment.getTotalAmount()
                .subtract(newPaidAmount);

        payment.setPaidAmount(newPaidAmount);
        payment.setRemainingAmount(newRemainingAmount);

        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            payment.setPaymentStatus(PaymentStatus.PAID);
        } else {
            payment.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        }

        paymentRepository.save(payment);

        long number = refundRepository.getNextRefundNumber();
        String refundNumber = String.format("REF-%05d", number);

        Refund refund = Refund.builder()
                .refundNumber(refundNumber)
                .paymentTransaction(transaction)
                .refundAmount(request.getRefundAmount())
                .refundStatus(status)
                .refundReason(request.getRefundReason())
                .refundDate(LocalDateTime.now())
                .build();

        Refund savedRefund = refundRepository.save(refund);

        log.info("Refund created successfully with id: {}",
                savedRefund.getRefundId());

        return mapToResponse(savedRefund);
    }

    @Override
    @Transactional(readOnly = true)
    public RefundResponse getRefundById(Long refundId) {

        log.info("Fetching refund with id: {}", refundId);

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Refund not found with id: " + refundId));

        return mapToResponse(refund);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getRefundsByTransactionId(Long transactionId) {

        log.info("Fetching refunds for transaction id: {}", transactionId);

        if (!paymentTransactionRepository.existsById(transactionId)) {
            throw new ResourceNotFoundException(
                    "Payment transaction not found with id: " + transactionId);
        }

        return refundRepository
                .findByPaymentTransactionTransactionId(transactionId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getAllRefunds() {

        log.info("Fetching all refunds");

        return refundRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RefundResponse mapToResponse(Refund refund) {

        return RefundResponse.builder()
                .refundId(refund.getRefundId())
                .refundNumber(refund.getRefundNumber())
                .transactionId(refund.getPaymentTransaction().getTransactionId())
                .refundAmount(refund.getRefundAmount())
                .refundStatus(refund.getRefundStatus())
                .refundReason(refund.getRefundReason())
                .refundDate(refund.getRefundDate())
                .build();
    }
}