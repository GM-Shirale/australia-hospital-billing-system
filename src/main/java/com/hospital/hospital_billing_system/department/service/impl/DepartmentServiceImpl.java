package com.hospital.hospital_billing_system.department.service.impl;

import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.department.dto.DepartmentRequest;
import com.hospital.hospital_billing_system.department.dto.DepartmentResponse;
import com.hospital.hospital_billing_system.department.entity.Department;
import com.hospital.hospital_billing_system.department.repository.DepartmentRepository;
import com.hospital.hospital_billing_system.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of DepartmentService enforcing tenant isolation and unique department naming.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        // Business Rule: Prevent duplicate department names under the same hospital tenant
        departmentRepository.findByTenantIdAndDepartmentNameIgnoreCase(request.getTenantId(), request.getDepartmentName())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Department with name '" + request.getDepartmentName() + "' already exists for this hospital.");
                });

        Department department = Department.builder()
                .tenantId(request.getTenantId())
                .departmentName(request.getDepartmentName())
                .location(request.getLocation())
                .build();

        Department savedDepartment = departmentRepository.save(department);
        return mapToResponse(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartmentsByTenant(UUID tenantId) {
        return departmentRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(UUID departmentId, UUID tenantId) {
        Department department = departmentRepository.findByDepartmentIdAndTenantId(departmentId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(UUID departmentId, UUID tenantId, DepartmentRequest request) {
        Department department = departmentRepository.findByDepartmentIdAndTenantId(departmentId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));

        department.setDepartmentName(request.getDepartmentName());
        department.setLocation(request.getLocation());

        Department updated = departmentRepository.save(department);
        return mapToResponse(updated);
    }

    @Override
    public void deleteDepartment(UUID departmentId, UUID tenantId) {
        Department department = departmentRepository.findByDepartmentIdAndTenantId(departmentId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
        departmentRepository.delete(department);
    }

    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .departmentId(department.getDepartmentId())
                .tenantId(department.getTenantId())
                .departmentName(department.getDepartmentName())
                .location(department.getLocation())
                .build();
    }
}