package com.hospital.hospital_billing_system.doctor.service;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.department.entity.Department;
import com.hospital.hospital_billing_system.department.repository.DepartmentRepository;
import com.hospital.hospital_billing_system.doctor.dto.DoctorConsultationChargeRequest;
import com.hospital.hospital_billing_system.doctor.dto.DoctorConsultationChargeResponse;
import com.hospital.hospital_billing_system.doctor.dto.DoctorRequest;
import com.hospital.hospital_billing_system.doctor.dto.DoctorResponse;
import com.hospital.hospital_billing_system.doctor.entity.Doctor;
import com.hospital.hospital_billing_system.doctor.repository.DoctorRepository;
import com.hospital.hospital_billing_system.doctor.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test suite verifying Doctor business rules, Medicare compliance (FR-031),
 * and multi-tenant isolation boundaries.
 */
@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private UUID tenantId;
    private UUID departmentId;
    private UUID doctorId;
    private Department department;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        departmentId = UUID.randomUUID();
        doctorId = UUID.randomUUID();

        department = Department.builder()
                .departmentId(departmentId)
                .tenantId(tenantId)
                .departmentName("Cardiology")
                .location("Building A, Level 2")
                .build();

        doctor = Doctor.builder()
                .doctorId(doctorId)
                .tenantId(tenantId)
                .department(department)
                .firstName("John")
                .lastName("Smith")
                .specialization("Cardiologist")
                .providerNo("1234567A")
                .phone("0412345678")
                .email("john.smith@hospital.com.au")
                .build();
    }

    @Test
    @DisplayName("Should successfully register a doctor when payload and Medicare number are valid")
    void shouldRegisterDoctorSuccessfully() {
        DoctorRequest request = DoctorRequest.builder()
                .tenantId(tenantId)
                .departmentId(departmentId)
                .firstName("John")
                .lastName("Smith")
                .specialization("Cardiologist")
                .providerNo("1234567A")
                .build();

        when(doctorRepository.existsByProviderNo("1234567A")).thenReturn(false);
        when(departmentRepository.findByDepartmentIdAndTenantId(departmentId, tenantId))
                .thenReturn(Optional.of(department));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorResponse response = doctorService.registerDoctor(request);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("1234567A", response.getProviderNo());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when Medicare Provider Number already exists")
    void shouldThrowExceptionWhenProviderNumberIsDuplicate() {
        DoctorRequest request = DoctorRequest.builder()
                .tenantId(tenantId)
                .departmentId(departmentId)
                .providerNo("1234567A")
                .build();

        when(doctorRepository.existsByProviderNo("1234567A")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> doctorService.registerDoctor(request));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when Department belongs to another tenant")
    void shouldEnforceTenantIsolationOnDepartmentAssignment() {
        DoctorRequest request = DoctorRequest.builder()
                .tenantId(tenantId)
                .departmentId(departmentId)
                .providerNo("9999999Z")
                .build();

        when(doctorRepository.existsByProviderNo("9999999Z")).thenReturn(false);
        // Department ID exists, but does not match the requesting tenantId
        when(departmentRepository.findByDepartmentIdAndTenantId(departmentId, tenantId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.registerDoctor(request));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    @DisplayName("SRS FR-031: Should successfully calculate consultation charge when doctor has valid Medicare number")
    void shouldCalculateConsultationChargeWhenMedicareNumberIsValid() {
        DoctorConsultationChargeRequest request = DoctorConsultationChargeRequest.builder()
                .doctorId(doctorId)
                .tenantId(tenantId)
                .consultationFee(new BigDecimal("150.00"))
                .build();

        when(doctorRepository.findByDoctorIdAndTenantId(doctorId, tenantId))
                .thenReturn(Optional.of(doctor));

        DoctorConsultationChargeResponse response = doctorService.verifyAndCalculateConsultationCharge(request);

        assertNotNull(response);
        assertTrue(response.isMbsBillable());
        assertEquals("1234567A", response.getProviderNo());
        assertEquals(new BigDecimal("150.00"), response.getConsultationFee());
    }

    @Test
    @DisplayName("SRS FR-031: Should throw IllegalStateException when doctor lacks Medicare number for MBS charge")
    void shouldBlockConsultationChargeWhenMedicareNumberIsMissing() {
        doctor.setProviderNo(null); // No Medicare Provider Number

        DoctorConsultationChargeRequest request = DoctorConsultationChargeRequest.builder()
                .doctorId(doctorId)
                .tenantId(tenantId)
                .consultationFee(new BigDecimal("150.00"))
                .build();

        when(doctorRepository.findByDoctorIdAndTenantId(doctorId, tenantId))
                .thenReturn(Optional.of(doctor));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> doctorService.verifyAndCalculateConsultationCharge(request)
        );

        assertTrue(exception.getMessage().contains("does not possess a valid Medicare Provider Number"));
    }
}