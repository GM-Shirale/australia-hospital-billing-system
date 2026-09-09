package com.hospital.hospital_billing_system.department.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Outbound response representation of a department record.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {

    private UUID departmentId;
    private UUID tenantId;
    private String departmentName;
    private String location;
}