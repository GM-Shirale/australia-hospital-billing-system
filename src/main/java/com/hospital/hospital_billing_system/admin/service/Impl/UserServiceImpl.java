package com.hospital.hospital_billing_system.admin.service.Impl;

import com.hospital.hospital_billing_system.admin.dto.UserRequest;
import com.hospital.hospital_billing_system.admin.dto.UserResponse;
import com.hospital.hospital_billing_system.admin.entity.User;
import com.hospital.hospital_billing_system.admin.repository.UserRepository;
import com.hospital.hospital_billing_system.admin.service.UserService;
import com.hospital.hospital_billing_system.common.exception.DuplicateResourceException;
import com.hospital.hospital_billing_system.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(UserRequest request) {

        log.info("Creating new user with username: {}", request.getUsername());

        // check duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException(
                    "Username already exists: " + request.getUsername());
        }

        // check duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists: " + request.getEmail());
        }

        // create user
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole())
                .active(false)
                .build();

        User savedUser = userRepository.save(user);

        log.info("User created successfully with id: {}", savedUser.getUserId());

        return mapToResponse(savedUser);
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }

    @Override
    public UserResponse getUserById(Long userId) {

        log.info("Fetching user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        return mapToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        log.info("Fetching all users");

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest request) {

        log.info("Updating user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        // check username only if it is changed
        if (!user.getUsername().equals(request.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {

            throw new DuplicateResourceException(
                    "Username already exists: " + request.getUsername());
        }

        // check email only if it is changed
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists: " + request.getEmail());
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        // update password only when a new password is provided
        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        log.info("User updated successfully with id: {}", userId);

        return mapToResponse(updatedUser);
    }

    @Override
    public UserResponse activateUser(Long userId) {

        log.info("Activating user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        user.setActive(true);

        User updatedUser = userRepository.save(user);

        log.info("User activated successfully with id: {}", userId);

        return mapToResponse(updatedUser);
    }


    public UserResponse deactivateUser(Long userId) {

        log.info("Deactivating user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        user.setActive(false);

        User updatedUser = userRepository.save(user);

        log.info("User deactivated successfully with id: {}", userId);

        return mapToResponse(updatedUser);
    }


    public void deleteUser(Long userId) {

        log.info("Deleting user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        userRepository.delete(user);

        log.info("User deleted successfully with id: {}", userId);
    }

}