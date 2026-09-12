package com.hospital.hospital_billing_system.admin.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequest {

    private String username;

    private String password;

}
