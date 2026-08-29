package com.hospital.hospital_billing_system.patient.dto;

import com.hospital.hospital_billing_system.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMedicareNumber() {
        return medicareNumber;
    }

    public void setMedicareNumber(String medicareNumber) {
        this.medicareNumber = medicareNumber;
    }

    public String getMedicareIrn() {
        return medicareIrn;
    }

    public void setMedicareIrn(String medicareIrn) {
        this.medicareIrn = medicareIrn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }
}