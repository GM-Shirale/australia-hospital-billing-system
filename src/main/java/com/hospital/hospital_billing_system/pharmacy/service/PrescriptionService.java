package com.hospital.hospital_billing_system.pharmacy.service;


import com.hospital.hospital_billing_system.common.enums.PrescriptionStatus;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionResponseDto;

import java.util.List;
import java.util.UUID;

public interface PrescriptionService {

    PrescriptionResponseDto createPrescription(PrescriptionRequestDto request);

    PrescriptionResponseDto getPrescriptionById(
            Long prescriptionId
    );

    List<PrescriptionResponseDto> getAllPrescriptions();

    List<PrescriptionResponseDto> getPrescriptionsByPatientId(
            Long patientId
    );

    List<PrescriptionResponseDto> getPrescriptionsByDoctorId(
            UUID doctorId
    );

    PrescriptionResponseDto updatePrescription(
            Long prescriptionId,
            PrescriptionRequestDto request
    );
}
