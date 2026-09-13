package com.hospital.hospital_billing_system.admin.dto;

import com.hospital.hospital_billing_system.admin.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    // username used for login
    private String username;

    // password provided during user creation
    private String password;

    // user email
    private String email;

    // role assigned to the user
    private Role role;

    private  Boolean active;
}