package com.hospital.hospital_billing_system.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {

    private Long adminId;

    private String username;

    private String email;

    private Boolean active;
}