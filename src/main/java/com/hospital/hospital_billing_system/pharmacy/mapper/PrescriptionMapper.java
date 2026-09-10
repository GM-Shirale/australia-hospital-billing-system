package com.hospital.hospital_billing_system.pharmacy.mapper;

import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionRequestDto;
import com.hospital.hospital_billing_system.pharmacy.dto.PrescriptionResponseDto;
import com.hospital.hospital_billing_system.pharmacy.entity.Prescription;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    public Prescription toEntity(PrescriptionRequestDto request){
        return Prescription.builder()
                .prescriptionDate(request.getPrescriptionDate())
                .notes(request.getNotes())
                .build();
    }

    public PrescriptionResponseDto toResponseDto(
            Prescription prescription
    ){
        return PrescriptionResponseDto.builder()
                .prescriptionId(prescription.getPrescriptionId())
                .patientId(prescription.getPatient().getPatientId())
                .doctorId(prescription.getDoctor().getDoctorId())
                .prescriptionDate(prescription.getPrescriptionDate())
                .notes(prescription.getNotes())
                .status(prescription.getStatus())
                .createdAt(prescription.getCreatedAt())
                .updatedAt(prescription.getUpdatedAt())
                .build();
    }
}
