package com.hospital.hospital_billing_system.patient.service.impl;

import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.patient.dto.PatientRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientResponse;
import com.hospital.hospital_billing_system.patient.entity.Patient;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import com.hospital.hospital_billing_system.patient.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    // constructor injection
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponse createPatient(PatientRequest request) {

        // check duplicate email
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Patient with email already exists: " + request.getEmail()
            );
        }

        // check duplicate phone
        if (patientRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException(
                    "Patient with phone number already exists: " + request.getPhone()
            );
        }

        // check duplicate Medicare number
        if (patientRepository.existsByMedicareNumber(request.getMedicareNumber())) {
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

        // convert entity to response
        return mapToResponse(savedPatient);
    }

    @Override
    public PatientResponse getPatientById(Long patientId) {

        // find patient by id
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId  )
                                       );

        return mapToResponse(patient);
    }

    @Override
    public List<PatientResponse> getAllPatients() {

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

        // find existing patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId
                        )                );

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

        return mapToResponse(updatedPatient);
    }

    @Override
    public void deletePatient(Long patientId) {

        // check patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId
            );
        }

        // delete patient
        patientRepository.deleteById(patientId);
    }

    // generate unique patient number
    private String generatePatientNumber() {

        long number = patientRepository.count() + 1;

        return String.format("PAT-%05d", number);
    }

    // convert patient entity to response DTO
    private PatientResponse mapToResponse(Patient patient) {

        PatientResponse response = new PatientResponse();

        response.setPatientId(patient.getPatientId());
        response.setPatientNumber(patient.getPatientNumber());
        response.setFirstName(patient.getFirstName());
        response.setMiddleName(patient.getMiddleName());
        response.setLastName(patient.getLastName());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(patient.getGender());
        response.setMedicareNumber(patient.getMedicareNumber());
        response.setMedicareIrn(patient.getMedicareIrn());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        response.setEmergencyContactName(patient.getEmergencyContactName());
        response.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        response.setStatus(patient.getStatus());
        response.setCreatedAt(patient.getCreatedAt());
        response.setUpdatedAt(patient.getUpdatedAt());

        return response;
    }
}