package com.hospital.hospital_billing_system.patient.dto;

import com.hospital.hospital_billing_system.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PatientRequest {

    // first name of patient
    @NotBlank(message = "First name is required")
    private String firstName;

    // middle name of patient
    private String middleName;

    // last name of patient
    @NotBlank(message = "Last name is required")
    private String lastName;

    // date of birth
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    // gender of patient
    @NotNull(message = "Gender is required")
    private Gender gender;

    // Medicare number
    private String medicareNumber;

    // Medicare individual reference number
    private String medicareIrn;

    // email of patient
    @Email(message = "Invalid email format")
    private String email;

    // phone number
    private String phone;

    // emergency contact name
    private String emergencyContactName;

    // emergency contact phone
    private String emergencyContactPhone;

}