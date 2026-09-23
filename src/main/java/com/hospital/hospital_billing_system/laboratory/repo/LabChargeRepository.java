package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.common.enums.BillingType;
import com.hospital.hospital_billing_system.common.enums.LabChargeStatus;
import com.hospital.hospital_billing_system.laboratory.entity.LabCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LabChargeRepository
        extends JpaRepository<LabCharge, Long> {

    Optional<LabCharge> findByChargeNumber(String chargeNumber);

    boolean existsByChargeNumber(String chargeNumber);

    List<LabCharge> findByLabOrderItemId(Long labOrderItemId);

    List<LabCharge> findByStatus(LabChargeStatus status);

    List<LabCharge> findByBillingType(BillingType billingType);

    @Query("""
            SELECT COALESCE(SUM(lc.providerCharge), 0)
            FROM LabCharge lc
            WHERE lc.labOrderItem.labOrder.id = :labOrderId
            """)
    BigDecimal getTotalProviderChargeByLabOrderId(
            @Param("labOrderId") Long labOrderId
    );

    @Query("""
            SELECT COALESCE(SUM(lc.medicareBenefit), 0)
            FROM LabCharge lc
            WHERE lc.labOrderItem.labOrder.id = :labOrderId
            """)
    BigDecimal getTotalMedicareBenefitByLabOrderId(
            @Param("labOrderId") Long labOrderId
    );

    @Query("""
            SELECT COALESCE(SUM(lc.patientAmount), 0)
            FROM LabCharge lc
            WHERE lc.labOrderItem.labOrder.id = :labOrderId
            """)
    BigDecimal getTotalPatientAmountByLabOrderId(
            @Param("labOrderId") Long labOrderId
    );
}