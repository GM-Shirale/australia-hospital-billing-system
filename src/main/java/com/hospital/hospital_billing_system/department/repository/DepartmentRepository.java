package com.hospital.hospital_billing_system.department.repository;

import com.hospital.hospital_billing_system.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {


    List<Department> findByTenantId(UUID tenantId);


    Optional<Department> findByTenantIdAndDepartmentNameIgnoreCase(UUID tenantId, String departmentName);

    Optional<Department> findByDepartmentIdAndTenantId(UUID departmentId, UUID tenantId);
}
