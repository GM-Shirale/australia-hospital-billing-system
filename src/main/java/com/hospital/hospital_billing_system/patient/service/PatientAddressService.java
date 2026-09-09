package com.hospital.hospital_billing_system.patient.service;


import com.hospital.hospital_billing_system.patient.dto.PatientAddressRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientAddressResponse;

import java.util.List;

public interface PatientAddressService {

    // create address for patient
    PatientAddressResponse createAddress(Long patientId, PatientAddressRequest request);

    // get address by id
    PatientAddressResponse getAddressById(Long addressId);

    // get all addresses of patient
    List<PatientAddressResponse> getAddressesByPatientId(Long patientId);

    // update patient address
    PatientAddressResponse updateAddress(Long addressId,PatientAddressRequest request);

    // delete patient address
    void deleteAddress(Long addressId);
}