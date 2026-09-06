package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;

import java.util.List;
import java.util.Optional;

public interface LabOrderRepository extends JpaRepository<LabOrder, Long> {

    Optional<LabOrder> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    boolean existsByOrderNumberAndIdNot(String orderNumber,Long id);


}