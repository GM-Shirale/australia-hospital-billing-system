package com.hospital.hospital_billing_system.admin.service;

import com.hospital.hospital_billing_system.admin.dto.AdminRequest;
import com.hospital.hospital_billing_system.admin.dto.AdminResponse;

public interface AdminService {

    // create admin account
    AdminResponse createAdmin(AdminRequest request);

    // get admin by id
    AdminResponse getAdminById(Long adminId);

    // update admin
    AdminResponse updateAdmin(Long adminId, AdminRequest request);

    // activate admin
    AdminResponse activateAdmin(Long adminId);

    // deactivate admin
    AdminResponse deactivateAdmin(Long adminId);
}