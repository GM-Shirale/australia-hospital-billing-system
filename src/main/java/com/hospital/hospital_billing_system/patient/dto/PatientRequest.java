package com.hospital.hospital_billing_system.patient.dto;

import com.hospital.hospital_billing_system.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PatientRequest {

    // first name of patient
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    // middle name of patient
    @Size(max = 100, message = "Middle name must not exceed 100 characters")
    private String middleName;

    // last name of patient
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    // date of birth
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // gender of patient
    @NotNull(message = "Gender is required")
    private Gender gender;

    // Medicare number - exactly 10 digits
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Medicare number must contain exactly 10 digits"
    )
    private String medicareNumber;

    // Medicare individual reference number - exactly 1 digit
    @Pattern(
            regexp = "^\\d$",
            message = "Medicare IRN must contain exactly 1 digit"
    )
    private String medicareIrn;

    // email of patient
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    // phone number - exactly 10 digits
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phone;

    // emergency contact name
    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    private String emergencyContactName;

    // emergency contact phone - exactly 10 digits
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Emergency contact phone must contain exactly 10 digits"
    )
    private String emergencyContactPhone;
}