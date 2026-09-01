package com.hospital.hospital_billing_system.patient.entity;

import com.hospital.hospital_billing_system.common.enums.Gender;
import com.hospital.hospital_billing_system.common.enums.PatientStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a patient registered in the hospital.
 */
@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    /**
     * Primary key of the patient table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    /**
     * Unique hospital-generated patient number.
     * Example: PAT-10001
     */
    @Column(
            name = "patient_number",
            nullable = false,
            unique = true,
            length = 50
    )
    private String patientNumber;

    /**
     * Patient first name.
     */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /**
     * Patient middle name.
     * This field is optional.
     */
    @Column(name = "middle_name", length = 100)
    private String middleName;

    /**
     * Patient last name.
     */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /**
     * Patient date of birth.
     */
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    /**
     * Patient gender.
     *
     * EnumType.STRING stores values such as:
     * MALE, FEMALE, OTHER
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 30)
    private Gender gender;

    /**
     * Australian Medicare number.
     */
    @Column(name = "medicare_number", length = 20)
    private String medicareNumber;

    /**
     * Medicare Individual Reference Number.
     */
    @Column(name = "medicare_irn", length = 10)
    private String medicareIrn;

    /**
     * Patient email address.
     */
    @Column(name = "email", length = 255)
    @Email
    private String email;

    /**
     * Patient phone number.
     */

    @Column(name = "phone", length = 30)
    private String phone;

    /**
     * Emergency contact person's name.
     */
    @Column(name = "emergency_contact_name", length = 200)
    private String emergencyContactName;

    /**
     * Emergency contact person's phone number.
     */
    @Column(name = "emergency_contact_phone", length = 30)
    private String emergencyContactPhone;

    /**
     * Current status of the patient.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PatientStatus status;

    /**
     * Date and time when patient was created.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Date and time when patient was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Executes automatically before inserting a new patient.
     */
    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // Set ACTIVE as the default patient status.
        if (status == null) {
            status = PatientStatus.ACTIVE;
        }
    }

    /**
     * Executes automatically before updating a patient.
     */
    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}