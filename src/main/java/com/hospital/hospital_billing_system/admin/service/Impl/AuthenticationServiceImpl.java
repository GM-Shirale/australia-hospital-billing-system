package com.hospital.hospital_billing_system.admin.service.Impl;


import com.hospital.hospital_billing_system.admin.dto.AdminLoginRequest;
import com.hospital.hospital_billing_system.admin.dto.LoginResponse;
import com.hospital.hospital_billing_system.admin.dto.UserLoginRequest;
import com.hospital.hospital_billing_system.admin.entity.Admin;
import com.hospital.hospital_billing_system.admin.entity.User;
import com.hospital.hospital_billing_system.admin.repository.AdminRepository;
import com.hospital.hospital_billing_system.admin.repository.UserRepository;
import com.hospital.hospital_billing_system.admin.service.AuthenticationService;
import com.hospital.hospital_billing_system.config.JwtService;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(
            AdminRepository adminRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse adminLogin(AdminLoginRequest request) {

        Admin admin = adminRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found"));

        if (!admin.getActive()) {
            throw new IllegalStateException("Admin account is inactive");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                admin.getPassword())) {

            throw new IllegalStateException("Invalid username or password");
        }

        String token = jwtService.generateToken(
                admin.getUsername(),
                "ADMIN"
        );

        log.info("Admin logged in successfully: {}", admin.getUsername());

        return LoginResponse.builder()
                .token(token)
                .username(admin.getUsername())
                .role("ADMIN")
                .build();
    }

    @Override
    public LoginResponse userLogin(UserLoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!user.getActive()) {
            throw new IllegalStateException("User account is inactive");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new IllegalStateException("Invalid username or password");
        }

        String token = jwtService.generateToken(
                user.getUsername(),
                user.getRole().name()
        );

        log.info("User logged in successfully: {}", user.getUsername());

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}