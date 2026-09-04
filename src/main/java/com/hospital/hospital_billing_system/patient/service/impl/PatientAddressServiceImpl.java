package com.hospital.hospital_billing_system.patient.service.impl;


import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import com.hospital.hospital_billing_system.patient.dto.PatientAddressRequest;
import com.hospital.hospital_billing_system.patient.dto.PatientAddressResponse;
import com.hospital.hospital_billing_system.patient.entity.Patient;
import com.hospital.hospital_billing_system.patient.entity.PatientAddress;
import com.hospital.hospital_billing_system.patient.repository.PatientAddressRepository;
import com.hospital.hospital_billing_system.patient.repository.PatientRepository;
import com.hospital.hospital_billing_system.patient.service.PatientAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientAddressServiceImpl implements PatientAddressService {

    private static final Logger log =
            LoggerFactory.getLogger(PatientAddressServiceImpl.class);

    private final PatientAddressRepository patientAddressRepository;
    private final PatientRepository patientRepository;

    // constructor injection
    public PatientAddressServiceImpl(PatientAddressRepository patientAddressRepository, PatientRepository patientRepository) {

        this.patientAddressRepository = patientAddressRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientAddressResponse createAddress(Long patientId, PatientAddressRequest request) {

        log.info("Creating address for patient with id: {}", patientId);

        // find patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId
                        )
                );

        // create address using builder
        PatientAddress address = PatientAddress.builder()
                .addressType(request.getAddressType())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .suburb(request.getSuburb())
                .state(request.getState())
                .postcode(request.getPostcode())
                .country(request.getCountry())
                .patient(patient)
                .build();

        // save address
        PatientAddress savedAddress =
                patientAddressRepository.save(address);

        log.info("Patient address created successfully");

        return mapToResponse(savedAddress);
    }

    @Override
    public PatientAddressResponse getAddressById(Long addressId) {

        log.info("Fetching patient address with id: {}", addressId);

        // find address by id
        PatientAddress address = patientAddressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient address not found with id: " + addressId
                        )
                );

        return mapToResponse(address);
    }

    @Override
    public List<PatientAddressResponse> getAddressesByPatientId(
            Long patientId) {

        log.info("Fetching addresses for patient with id: {}", patientId);

        // check patient exists
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        // get all addresses of patient
        return patientAddressRepository
                .findByPatientPatientId(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PatientAddressResponse updateAddress(
            Long addressId,
            PatientAddressRequest request) {

        log.info("Updating patient address with id: {}", addressId);

        // find existing address
        PatientAddress address = patientAddressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient address not found with id: " + addressId
                        )
                );

        // update address details
        address.setAddressType(request.getAddressType());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setSuburb(request.getSuburb());
        address.setState(request.getState());
        address.setPostcode(request.getPostcode());
        address.setCountry(request.getCountry());

        // save updated address
        PatientAddress updatedAddress =
                patientAddressRepository.save(address);

        log.info("Patient address updated successfully with id: {}", addressId);

        return mapToResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {

        log.info("Deleting patient address with id: {}", addressId);

        // check address exists
        if (!patientAddressRepository.existsById(addressId)) {
            throw new ResourceNotFoundException(
                    "Patient address not found with id: " + addressId
            );
        }

        // delete address
        patientAddressRepository.deleteById(addressId);

        log.info("Patient address deleted successfully with id: {}", addressId);
    }
    // convert entity to response
    private PatientAddressResponse mapToResponse(PatientAddress address) {

        return PatientAddressResponse.builder()
                .addressId(address.getAddressId())
                .patientId(address.getPatient().getPatientId())
                .addressType(address.getAddressType())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .suburb(address.getSuburb())
                .state(address.getState())
                .postcode(address.getPostcode())
                .country(address.getCountry())
                .build();
    }
}
