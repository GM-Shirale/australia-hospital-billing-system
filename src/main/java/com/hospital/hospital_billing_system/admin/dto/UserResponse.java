package com.hospital.hospital_billing_system.admin.dto;

import com.hospital.hospital_billing_system.admin.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long userId;

    private String username;

    private String email;

    private Role role;

    private Boolean active;
}