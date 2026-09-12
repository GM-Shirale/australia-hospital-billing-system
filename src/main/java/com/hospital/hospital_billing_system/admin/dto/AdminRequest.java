package com.hospital.hospital_billing_system.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRequest {

    // username used for admin login
    private String username;

    // password provided for admin account
    private String password;

    // admin email
    private String email;
}