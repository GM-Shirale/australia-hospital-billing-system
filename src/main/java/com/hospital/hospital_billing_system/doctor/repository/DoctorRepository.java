package com.hospital.hospital_billing_system.doctor.repository;

import com.hospital.hospital_billing_system.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository providing database access methods for Doctor entities.
 * Includes tenant-scoped queries to prevent accidental cross-tenant data access.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    // Retrieve all active doctors belonging to a specific hospital tenant
    List<Doctor> findByTenantId(UUID tenantId);

    // Fetch doctors assigned to a specific department within a hospital
    List<Doctor> findByTenantIdAndDepartment_DepartmentId(UUID tenantId, UUID departmentId);

    // Look up a practitioner by their unique Australian Medicare Provider Number
    Optional<Doctor> findByProviderNo(String providerNo);

    // Check if a provider number already exists to prevent duplicate registrations
    boolean existsByProviderNo(String providerNo);

    // Safely fetch a single doctor ensuring they belong to the authenticated hospital tenant
    Optional<Doctor> findByDoctorIdAndTenantId(UUID doctorId, UUID tenantId);
}