package com.hospital.hospital_billing_system.admin.service;

import com.hospital.hospital_billing_system.admin.dto.AdminLoginRequest;
import com.hospital.hospital_billing_system.admin.dto.LoginResponse;
import com.hospital.hospital_billing_system.admin.dto.UserLoginRequest;

public interface AuthenticationService {

    LoginResponse adminLogin(AdminLoginRequest request);

    LoginResponse userLogin(UserLoginRequest request);
}