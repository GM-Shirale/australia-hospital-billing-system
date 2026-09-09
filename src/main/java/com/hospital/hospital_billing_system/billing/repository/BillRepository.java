package com.hospital.hospital_billing_system.billing.repository;

import com.hospital.hospital_billing_system.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query(value = "SELECT nextval('bill_number_seq')", nativeQuery = true)
    long getNextBillNumber();

    // get all bills for a patient
    List<Bill> findByPatientPatientId(Long patientId);
}