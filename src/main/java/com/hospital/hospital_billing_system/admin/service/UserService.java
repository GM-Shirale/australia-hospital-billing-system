package com.hospital.hospital_billing_system.admin.service;

import com.hospital.hospital_billing_system.admin.dto.UserRequest;
import com.hospital.hospital_billing_system.admin.dto.UserResponse;

import java.util.List;

public interface UserService {

    // create new system user
    UserResponse createUser(UserRequest request);

    // get user by id
    UserResponse getUserById(Long userId);

    // get all users
    List<UserResponse> getAllUsers();

    // update user
    UserResponse updateUser(Long userId, UserRequest request);

    // activate user
    UserResponse activateUser(Long userId);

    // deactivate user
    UserResponse deactivateUser(Long userId);

    // delete user
    void deleteUser(Long userId);
}