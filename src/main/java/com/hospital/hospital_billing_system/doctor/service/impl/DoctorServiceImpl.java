package com.hospital.hospital_billing_system.doctor.service.impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.department.entity.Department;
import com.hospital.hospital_billing_system.department.repository.DepartmentRepository;
import com.hospital.hospital_billing_system.doctor.dto.DoctorRequest;
import com.hospital.hospital_billing_system.doctor.dto.DoctorResponse;
import com.hospital.hospital_billing_system.doctor.entity.Doctor;
import com.hospital.hospital_billing_system.doctor.repository.DoctorRepository;
import com.hospital.hospital_billing_system.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of DoctorService enforcing Medicare billing validity and tenant boundaries.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public DoctorResponse registerDoctor(DoctorRequest request) {
        // Business Rule: Ensure Medicare Provider Number is globally unique
        if (doctorRepository.existsByProviderNo(request.getProviderNo())) {
            throw new DuplicateResourceException(
                    "Doctor with Medicare Provider Number " + request.getProviderNo() + " already exists.");
        }

        // Business Rule: Ensure Department exists and belongs strictly to the requested hospital tenant
        Department department = departmentRepository.findByDepartmentIdAndTenantId(request.getDepartmentId(), request.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + request.getDepartmentId() + " for this hospital tenant."));

        Doctor doctor = Doctor.builder()
                .tenantId(request.getTenantId())
                .department(department)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .specialization(request.getSpecialization())
                .phone(request.getPhone())
                .email(request.getEmail())
                .providerNo(request.getProviderNo())
                .build();

        Doctor saved = doctorRepository.save(doctor);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(UUID doctorId, UUID tenantId) {
        Doctor doctor = doctorRepository.findByDoctorIdAndTenantId(doctorId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        return mapToResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getDoctorByProviderNumber(String providerNo) {
        Doctor doctor = doctorRepository.findByProviderNo(providerNo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with Medicare Provider Number: " + providerNo));
        return mapToResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsByTenant(UUID tenantId) {
        return doctorRepository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponse> getDoctorsByDepartment(UUID tenantId, UUID departmentId) {
        return doctorRepository.findByTenantIdAndDepartment_DepartmentId(tenantId, departmentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponse updateDoctor(UUID doctorId, UUID tenantId, DoctorRequest request) {
        Doctor doctor = doctorRepository.findByDoctorIdAndTenantId(doctorId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        // Re-validate department if changed
        Department department = departmentRepository.findByDepartmentIdAndTenantId(request.getDepartmentId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found with ID: " + request.getDepartmentId()));

        doctor.setDepartment(department);
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());

        // Update provider number only if changed and does not collide
        if (!doctor.getProviderNo().equalsIgnoreCase(request.getProviderNo())) {
            if (doctorRepository.existsByProviderNo(request.getProviderNo())) {
                throw new DuplicateResourceException(
                        "Medicare Provider Number " + request.getProviderNo() + " is already in use.");
            }
            doctor.setProviderNo(request.getProviderNo());
        }

        Doctor updated = doctorRepository.save(doctor);
        return mapToResponse(updated);
    }

    @Override
    public void deleteDoctor(UUID doctorId, UUID tenantId) {
        Doctor doctor = doctorRepository.findByDoctorIdAndTenantId(doctorId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        doctorRepository.delete(doctor);
    }

    private DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .doctorId(doctor.getDoctorId())
                .tenantId(doctor.getTenantId())
                .departmentId(doctor.getDepartment().getDepartmentId())
                .departmentName(doctor.getDepartment().getDepartmentName())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .providerNo(doctor.getProviderNo())
                .build();
    }
}