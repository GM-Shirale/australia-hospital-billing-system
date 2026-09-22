package com.hospital.hospital_billing_system.billing.repository;

import com.hospital.hospital_billing_system.billing.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    List<PaymentTransaction> findByPaymentPaymentId(Long paymentId);

    @Query(value = "SELECT nextval('payment_transaction_number_seq')", nativeQuery = true)
    long getNextTransactionNumber();
}