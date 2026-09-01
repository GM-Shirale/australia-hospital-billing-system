package com.hospital.hospital_billing_system.patient.service.impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.patient.dto.PatientRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientResponse;
import com.hospital.hospital_billing_system.patient.entity.Patient;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import com.hospital.hospital_billing_system.patient.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private static final Logger log =
            LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;

    // constructor injection
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponse createPatient(PatientRequest request) {

        log.info("Creating new patient");

        // check duplicate email
        if (patientRepository.existsByEmail(request.getEmail())) {
            log.warn("Duplicate patient email found");

            throw new DuplicateResourceException(
                    "Patient with email already exists: " + request.getEmail()
            );
        }

        // check duplicate phone
        if (patientRepository.existsByPhone(request.getPhone())) {
            log.warn("Duplicate patient phone number found");

            throw new DuplicateResourceException(
                    "Patient with phone number already exists: " + request.getPhone()
            );
        }

        // check duplicate Medicare number
        if (patientRepository.existsByMedicareNumber(request.getMedicareNumber())) {
            log.warn("Duplicate patient Medicare number found");

            throw new DuplicateResourceException(
                    "Patient with Medicare number already exists: "
                            + request.getMedicareNumber()
            );
        }

        // create patient using builder
        Patient patient = Patient.builder()
                .patientNumber(generatePatientNumber())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .medicareNumber(request.getMedicareNumber())
                .medicareIrn(request.getMedicareIrn())
                .email(request.getEmail())
                .phone(request.getPhone())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .build();

        // save patient
        Patient savedPatient = patientRepository.save(patient);

        log.info("Patient created successfully");

        return mapToResponse(savedPatient);
    }

    @Override
    public PatientResponse getPatientById(Long patientId) {

        log.info("Fetching patient with id: {}", patientId);

        // find patient by id
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId
                        )
                );

        return mapToResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {

        log.info("Fetching all patients");

        // get all patients
        return patientRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PatientResponse updatePatient(
            Long patientId,
            PatientRequest request
    ) {

        log.info("Updating patient with id: {}", patientId);

        // find existing patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId
                        )
                );

        // update patient details
        patient.setFirstName(request.getFirstName());
        patient.setMiddleName(request.getMiddleName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setMedicareNumber(request.getMedicareNumber());
        patient.setMedicareIrn(request.getMedicareIrn());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());

        // save updated patient
        Patient updatedPatient = patientRepository.save(patient);

        log.info("Patient updated successfully with id: {}", patientId);

        return mapToResponse(updatedPatient);
    }

    @Override
    public void deletePatient(Long patientId) {

        log.info("Deleting patient with id: {}", patientId);

        // check patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId
            );
        }

        // delete patient
        patientRepository.deleteById(patientId);

        log.info("Patient deleted successfully with id: {}", patientId);
    }

    // generate unique patient number
    private String generatePatientNumber() {

        long number = patientRepository.count() + patientRepository.getNextPatientNumber();

        return String.format("PAT-%05d", number);
    }

    // convert entity to response
    private PatientResponse mapToResponse(Patient patient) {

        return PatientResponse.builder()
                .patientId(patient.getPatientId())
                .patientNumber(patient.getPatientNumber())
                .firstName(patient.getFirstName())
                .middleName(patient.getMiddleName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .medicareNumber(patient.getMedicareNumber())
                .medicareIrn(patient.getMedicareIrn())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .status(patient.getStatus())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}