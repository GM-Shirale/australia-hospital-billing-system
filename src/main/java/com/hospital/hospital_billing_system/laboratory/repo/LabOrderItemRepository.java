package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabOrderItemRepository extends JpaRepository<LabOrderItem, Long> {

    List<LabOrderItem> findByLabOrderId(Long labOrderId);

    boolean existsByLabOrderIdAndLabTestId(
            Long labOrderId,
            Long LabTestId
    );


}