package com.hospital.hospital_billing_system.admin.service.Impl;

import com.hospital.hospital_billing_system.admin.dto.AdminLoginRequest;
import com.hospital.hospital_billing_system.admin.dto.LoginResponse;
import com.hospital.hospital_billing_system.admin.dto.UserLoginRequest;
import com.hospital.hospital_billing_system.admin.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * DEV MODE — no DB lookup, no password check, no JWT.
 * Any username/password is accepted.
 * Replace with the real implementation before deploying to production.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public LoginResponse adminLogin(AdminLoginRequest request) {
        log.info("[DEV] Admin login bypassed for user: {}", request.getUsername());
        return LoginResponse.builder()
                .token("dev-token-admin")
                .username(request.getUsername())
                .role("ADMIN")
                .build();
    }

    @Override
    public LoginResponse userLogin(UserLoginRequest request) {
        log.info("[DEV] User login bypassed for user: {}", request.getUsername());
        return LoginResponse.builder()
                .token("dev-token-user")
                .username(request.getUsername())
                .role("DOCTOR")
                .build();
    }
}