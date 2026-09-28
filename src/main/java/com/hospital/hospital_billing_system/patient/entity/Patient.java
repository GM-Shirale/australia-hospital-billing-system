package com.hospital.hospital_billing_system.patient.entity;

import com.hospital.hospital_billing_system.common.enums.Gender;
import com.hospital.hospital_billing_system.common.enums.PatientStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(
            name = "patient_number",
            nullable = false,
            unique = true,
            length = 50
    )
    private String patientNumber;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100, message = "Middle name must not exceed 100 characters")
    @Column(name = "middle_name", length = 100)
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Past(message = "Date of birth must be a past date")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 30)
    private Gender gender;

    // Australian Medicare card number - exactly 10 digits.
    @Pattern(
            regexp = "^\\d{12}$",
            message = "Medicare number must contain exactly 10 digits"
    )
    @Column(name = "medicare_number", length = 20, unique = true)
    private String medicareNumber;

    // Medicare Individual Reference Number - exactly 1 digit.
    @Pattern(
            regexp = "^\\d$",
            message = "Medicare IRN must contain exactly 1 digit"
    )
    @Column(name = "medicare_irn", length = 10)
    private String medicareIrn;

    @Email(message = "Please provide a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Column(name = "email", length = 255, unique = true)
    private String email;

    // Phone number must contain exactly 10 digits.
    @Pattern(
            regexp = "^04\\d{8}$",
            message = "Australian mobile number must start with 04 and contain exactly 10 digits"
    )
    @Column(name = "phone", length = 10)
    private String phone;

    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    @Column(name = "emergency_contact_name", length = 10)
    private String emergencyContactName;

    // Emergency contact phone must contain exactly 10 digits.
    @Pattern(
            regexp = "^04\\d{8}$",
            message = "Australian mobile number must start with 04 and contain exactly 10 digits"
    )
    @Column(name = "emergency_contact_phone", length = 10)
    private String emergencyContactPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PatientStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Executes automatically before inserting a new patient.
    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // Set ACTIVE as the default patient status.
        if (status == null) {
            status = PatientStatus.ACTIVE;
        }
    }

    // Executes automatically before updating a patient.
    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}