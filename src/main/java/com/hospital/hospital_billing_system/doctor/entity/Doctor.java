package com.hospital.hospital_billing_system.doctor.entity;

import com.hospital.hospital_billing_system.department.entity.Department;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Represents a medical practitioner / doctor in the hospital network.
 * Stores core credentials, department assignment, and Australian Medicare provider numbers.
 */
@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    // Unique primary identifier for the doctor record
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "doctor_id", updatable = false, nullable = false)
    private UUID doctorId;

    // Tenant identifier to guarantee strict multi-hospital data isolation
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    // Doctor personal demographic details
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    // Medical specialization (e.g., Cardiology, Orthopedics, Pediatrics)
    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;

    // Communication contact information
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 100)
    private String email;

    /**
     * Australian Medicare Provider Number.
     * Required by law and business rules (SRS FR-031) before raising any MBS billable charge.
     * Must be unique across practitioners.
     */
    @Column(name = "provider_no", nullable = false, unique = true, length = 20)
    private String providerNo;

    /**
     * Department association.
     * Many doctors can belong to one clinical department.
     * FetchType.LAZY ensures department details load on-demand to optimize database reads.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}