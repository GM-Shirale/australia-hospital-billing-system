package com.hospital.hospital_billing_system.pharmacy.service.Impl;

import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Prescription;
import com.hospital.hospital_billing_system.pharmacy.mapper.PrescriptionMapper;
import com.hospital.hospital_billing_system.pharmacy.repo.PrescriptionRepository;
import com.hospital.hospital_billing_system.pharmacy.repo.PrescriptionRepository;
import com.hospital.hospital_billing_system.pharmacy.service.PrescriptionService;
import com.hospital.hospital_billing_system.patient.entity.Patient;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import com.hospital.hospital_billing_system.doctor.entity.Doctor;
import com.hospital.hospital_billing_system.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Override
    public PrescriptionResponseDto createPrescription(
            PrescriptionRequestDto request) {

        log.info(
                "Creating prescription. Patient ID: {}, Doctor ID: {}",
                request.getPatientId(),
                request.getDoctorId()
        );

        validatePrescriptionDate(request);

        Patient patient = patientRepository
                .findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with ID: "
                                + request.getPatientId()
                ));

        Doctor doctor = doctorRepository
                .findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with ID: "
                                + request.getDoctorId()
                ));

        Prescription prescription =
                prescriptionMapper.toEntity(request);

        prescription.setPatient(patient);
        prescription.setDoctor(doctor);

        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        log.info(
                "Prescription created successfully. Prescription ID: {}",
                savedPrescription.getPrescriptionId()
        );

        return prescriptionMapper.toResponseDto(savedPrescription);
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponseDto getPrescriptionById(
            Long prescriptionId) {

        log.debug(
                "Fetching prescription with ID: {}",
                prescriptionId
        );

        Prescription prescription =
                prescriptionRepository.findById(prescriptionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found with ID: "
                                                + prescriptionId
                                ));

        return prescriptionMapper.toResponseDto(prescription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getAllPrescriptions() {

        log.debug("Fetching all prescriptions");

        return prescriptionRepository.findAll()
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getPrescriptionsByPatientId(
            Long patientId) {

        log.debug(
                "Fetching prescriptions for patient ID: {}",
                patientId
        );

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with ID: " + patientId
            );
        }

        return prescriptionRepository
                .findByPatientPatientId(patientId)
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .toList();
    }



    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponseDto> getPrescriptionsByDoctorId(
            UUID doctorId) {

        log.debug(
                "Fetching prescriptions for doctor ID: {}",
                doctorId
        );

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with ID: " + doctorId
            );
        }

        return prescriptionRepository
                .findByDoctorDoctorId(doctorId)
                .stream()
                .map(prescriptionMapper::toResponseDto)
                .toList();
    }
    @Override
    public PrescriptionResponseDto updatePrescription(
            Long prescriptionId,
            PrescriptionRequestDto request) {

        log.info(
                "Updating prescription with ID: {}",
                prescriptionId
        );

        validatePrescriptionDate(request);

        Prescription existingPrescription =
                prescriptionRepository.findById(prescriptionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Prescription not found with ID: "
                                                + prescriptionId
                                ));

        Patient patient = patientRepository
                .findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with ID: "
                                + request.getPatientId()
                ));

        Doctor doctor = doctorRepository
                .findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with ID: "
                                + request.getDoctorId()
                ));

        existingPrescription.setPatient(patient);
        existingPrescription.setDoctor(doctor);
        existingPrescription.setPrescriptionDate(
                request.getPrescriptionDate()
        );
        existingPrescription.setNotes(request.getNotes());

        Prescription updatedPrescription =
                prescriptionRepository.save(existingPrescription);

        log.info(
                "Prescription updated successfully. Prescription ID: {}",
                prescriptionId
        );

        return prescriptionMapper.toResponseDto(updatedPrescription);
    }

    private void validatePrescriptionDate(
            PrescriptionRequestDto request) {

        if (request.getPrescriptionDate() != null
                && request.getPrescriptionDate()
                .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Prescription date cannot be in the future"
            );
        }
    }
}