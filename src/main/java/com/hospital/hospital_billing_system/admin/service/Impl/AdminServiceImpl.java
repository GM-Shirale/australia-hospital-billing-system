package com.hospital.hospital_billing_system.admin.service.Impl;

import com.hospital.hospital_billing_system.admin.dto.AdminRequest;
import com.hospital.hospital_billing_system.admin.dto.AdminResponse;
import com.hospital.hospital_billing_system.admin.entity.Admin;
import com.hospital.hospital_billing_system.admin.repository.AdminRepository;
import com.hospital.hospital_billing_system.admin.service.AdminService;
import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger log =
            LoggerFactory.getLogger(AdminServiceImpl.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository,
                            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AdminResponse createAdmin(AdminRequest request) {

        // Check whether username is already used
        if (adminRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Admin username already exists");
        }

        // Check whether email is already used
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Admin email already exists");
        }

        // Create admin with encrypted password
        Admin admin = Admin.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .active(true)
                .build();

        Admin savedAdmin = adminRepository.save(admin);

        log.info("Admin created successfully with id: {}", savedAdmin.getAdminId());

        return AdminResponse.builder()
                .adminId(savedAdmin.getAdminId())
                .username(savedAdmin.getUsername())
                .email(savedAdmin.getEmail())
                .active(savedAdmin.getActive())
                .build();
    }

    @Override
    public AdminResponse getAdminById(Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found with id: " + adminId));

        log.info("Admin fetched successfully with id: {}", adminId);

        return AdminResponse.builder()
                .adminId(admin.getAdminId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .active(admin.getActive())
                .build();
    }

    @Override
    public AdminResponse updateAdmin(Long adminId, AdminRequest request) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found with id: " + adminId));

        // Check username only if it is changed
        if (!admin.getUsername().equals(request.getUsername())
                && adminRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Admin username already exists");
        }

        // Check email only if it is changed
        if (!admin.getEmail().equals(request.getEmail())
                && adminRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Admin email already exists");
        }

        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());

        // Update password only when a new password is provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Admin updatedAdmin = adminRepository.save(admin);

        log.info("Admin updated successfully with id: {}", adminId);

        return AdminResponse.builder()
                .adminId(updatedAdmin.getAdminId())
                .username(updatedAdmin.getUsername())
                .email(updatedAdmin.getEmail())
                .active(updatedAdmin.getActive())
                .build();
    }

    @Override
    public AdminResponse activateAdmin(Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found with id: " + adminId));

        admin.setActive(true);

        Admin updatedAdmin = adminRepository.save(admin);

        log.info("Admin activated successfully with id: {}", adminId);

        return AdminResponse.builder()
                .adminId(updatedAdmin.getAdminId())
                .username(updatedAdmin.getUsername())
                .email(updatedAdmin.getEmail())
                .active(updatedAdmin.getActive())
                .build();
    }

    @Override
    public AdminResponse deactivateAdmin(Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Admin not found with id: " + adminId));

        admin.setActive(false);

        Admin updatedAdmin = adminRepository.save(admin);

        log.info("Admin deactivated successfully with id: {}", adminId);

        return AdminResponse.builder()
                .adminId(updatedAdmin.getAdminId())
                .username(updatedAdmin.getUsername())
                .email(updatedAdmin.getEmail())
                .active(updatedAdmin.getActive())
                .build();
    }

}