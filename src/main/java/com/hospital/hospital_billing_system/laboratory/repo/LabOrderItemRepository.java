package com.hospital.hospital_billing_system.laboratory.repo;

import com.hospital.hospital_billing_system.laboratory.entity.LabOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabOrderItemRepository extends JpaRepository<LabOrderItem, Long> {
}