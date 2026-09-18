package com.hospital.hospital_billing_system.department.service;

import com.hospital.hospital_billing_system.department.dto.DepartmentRequest;
import com.hospital.hospital_billing_system.department.dto.DepartmentResponse;
import com.hospital.hospital_billing_system.department.entity.Department;
import com.hospital.hospital_billing_system.department.repository.DepartmentRepository;
import com.hospital.hospital_billing_system.department.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private UUID tenantId;
    private DepartmentRequest request;
    private Department department;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        request = DepartmentRequest.builder()
                .tenantId(tenantId)
                .departmentName("Cardiology")
                .location("Building A, Level 2")
                .build();

        department = Department.builder()
                .departmentId(UUID.randomUUID())
                .tenantId(tenantId)
                .departmentName("Cardiology")
                .location("Building A, Level 2")
                .build();
    }

    @Test
    @DisplayName("Should successfully create a new department under tenant")
    void shouldCreateDepartmentSuccessfully() {
        when(departmentRepository.findByTenantIdAndDepartmentNameIgnoreCase(tenantId, "Cardiology"))
                .thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        DepartmentResponse response = departmentService.createDepartment(request);

        assertNotNull(response);
        assertEquals("Cardiology", response.getDepartmentName());
        verify(departmentRepository, times(1)).save(any(Department.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when department name duplicates in same tenant")
    void shouldThrowExceptionWhenDuplicateDepartmentInTenant() {
        when(departmentRepository.findByTenantIdAndDepartmentNameIgnoreCase(tenantId, "Cardiology"))
                .thenReturn(Optional.of(department));

        assertThrows(IllegalArgumentException.class, () -> departmentService.createDepartment(request));
        verify(departmentRepository, never()).save(any(Department.class));
    }
}