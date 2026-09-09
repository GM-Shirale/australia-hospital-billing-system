package com.hospital.hospital_billing_system.admission.service.Impl;

import com.hospital.hospital_billing_system.admission.dto.AdmissionRequest;
import com.hospital.hospital_billing_system.admission.dto.AdmissionResponse;
import com.hospital.hospital_billing_system.admission.entity.Admission;
import com.hospital.hospital_billing_system.admission.entity.AdmissionStatus;
import com.hospital.hospital_billing_system.admission.repository.AdmissionRepository;
import com.hospital.hospital_billing_system.admission.service.AdmissionService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdmissionServiceImpl implements AdmissionService {

    private static final Logger log =
            LoggerFactory.getLogger(AdmissionServiceImpl.class);

    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;

    public AdmissionServiceImpl(
            AdmissionRepository admissionRepository,
            PatientRepository patientRepository) {
        this.admissionRepository = admissionRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public AdmissionResponse createAdmission(
            Long patientId,
            AdmissionRequest request) {

        log.info("Creating admission for patient with id: {}", patientId);

        // check whether patient exists
        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + patientId));

        // get next admission number
        long nextNumber = admissionRepository.getNextAdmissionNumber();

        String admissionNumber =
                String.format("ADM-%05d", nextNumber);

        // create admission
        Admission admission = Admission.builder()
                .admissionNumber(admissionNumber)
                .patient(patient)
                .admissionDate(request.getAdmissionDate())
                .admissionReason(request.getAdmissionReason())
                .status(request.getStatus())
                .build();

        Admission savedAdmission = admissionRepository.save(admission);

        log.info("Admission created successfully with id: {}",
                savedAdmission.getAdmissionId());

        return mapToResponse(savedAdmission);
    }

    private AdmissionResponse mapToResponse(Admission admission) {

        return AdmissionResponse.builder()
                .admissionId(admission.getAdmissionId())
                .admissionNumber(admission.getAdmissionNumber())
                .patientId(admission.getPatient().getPatientId())
                .admissionDate(admission.getAdmissionDate())
                .dischargeDate(admission.getDischargeDate())
                .admissionReason(admission.getAdmissionReason())
                .status(admission.getStatus())
                .build();
    }

    @Override
    public AdmissionResponse getAdmissionById(Long admissionId) {

        log.info("Fetching admission with id: {}", admissionId);

        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admission not found with id: " + admissionId));

        return mapToResponse(admission);
    }

    @Override
    public List<AdmissionResponse> getAllAdmissions() {

        log.info("Fetching all admissions");

        return admissionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AdmissionResponse> getAdmissionsByPatientId(Long patientId) {

        log.info("Fetching admissions for patient with id: {}", patientId);

        // check whether patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId);
        }

        return admissionRepository.findByPatientPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AdmissionResponse updateAdmission(Long admissionId, AdmissionRequest request) {

        log.info("Updating admission with id: {}", admissionId);

        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admission not found with id: " + admissionId));

        admission.setAdmissionDate(request.getAdmissionDate());
        admission.setAdmissionReason(request.getAdmissionReason());
        admission.setStatus(request.getStatus());

        Admission updatedAdmission = admissionRepository.save(admission);

        log.info("Admission updated successfully with id: {}", admissionId);

        return mapToResponse(updatedAdmission);
    }

    @Override
    public AdmissionResponse dischargeAdmission(Long admissionId) {

        log.info("Discharging admission with id: {}", admissionId);

        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Admission not found with id: " + admissionId));

        // update discharge details
        admission.setStatus(AdmissionStatus.DISCHARGED);
        admission.setDischargeDate(LocalDateTime.now());

        Admission updatedAdmission = admissionRepository.save(admission);

        log.info("Admission discharged successfully with id: {}", admissionId);

        return mapToResponse(updatedAdmission);
    }

    @Override
    public void deleteAdmission(Long admissionId) {

        log.info("Deleting admission with id: {}", admissionId);

        if (!admissionRepository.existsById(admissionId)) {
            throw new ResourceNotFoundException(
                    "Admission not found with id: " + admissionId);
        }

        admissionRepository.deleteById(admissionId);

        log.info("Admission deleted successfully with id: {}", admissionId);
    }
}