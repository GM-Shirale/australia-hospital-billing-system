package com.hospital.hospital_billing_system.department.service;

import com.hospital.hospital_billing_system.department.dto.DepartmentRequest;
import com.hospital.hospital_billing_system.department.dto.DepartmentResponse;

import java.util.List;
import java.util.UUID;

/**
 * Business service contract for managing hospital departments.
 */
public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest request);

    List<DepartmentResponse> getDepartmentsByTenant(UUID tenantId);

    DepartmentResponse getDepartmentById(UUID departmentId, UUID tenantId);

    DepartmentResponse updateDepartment(UUID departmentId, UUID tenantId, DepartmentRequest request);

    void deleteDepartment(UUID departmentId, UUID tenantId);
}
