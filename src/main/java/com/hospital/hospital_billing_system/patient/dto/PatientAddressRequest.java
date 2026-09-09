package com.hospital.hospital_billing_system.patient.dto;


import com.hospital.hospital_billing_system.common.enums.AddressType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientAddressRequest {

    // address type like HOME, WORK
    private AddressType addressType;

    // street address
    private String addressLine1;

    // additional address details
    private String addressLine2;

    // suburb name
    private String suburb;

    // state like NSW, VIC, QLD
    private String state;

    // Australian postcode
    private String postcode;

    // country name
    private String country;
}
