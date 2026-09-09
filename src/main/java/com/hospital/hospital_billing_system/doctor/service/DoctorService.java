package com.hospital.hospital_billing_system.doctor.service;

import com.hospital.hospital_billing_system.doctor.dto.DoctorConsultationChargeRequest;
import com.hospital.hospital_billing_system.doctor.dto.DoctorConsultationChargeResponse;
import com.hospital.hospital_billing_system.doctor.dto.DoctorRequest;
import com.hospital.hospital_billing_system.doctor.dto.DoctorResponse;

import java.util.List;
import java.util.UUID;

/**
 * Business service defining operations for Doctor and Provider credentialing.
 */
public interface DoctorService {

    DoctorResponse registerDoctor(DoctorRequest request);

    DoctorResponse getDoctorById(UUID doctorId, UUID tenantId);

    DoctorResponse getDoctorByProviderNumber(String providerNo);

    List<DoctorResponse> getDoctorsByTenant(UUID tenantId);

    List<DoctorResponse> getDoctorsByDepartment(UUID tenantId, UUID departmentId);

    DoctorResponse updateDoctor(UUID doctorId, UUID tenantId, DoctorRequest request);

    void deleteDoctor(UUID doctorId, UUID tenantId);

    // Method to validate practitioner credentials and calculate consultation charge payload for Central Billing
    DoctorConsultationChargeResponse verifyAndCalculateConsultationCharge(DoctorConsultationChargeRequest request);


}