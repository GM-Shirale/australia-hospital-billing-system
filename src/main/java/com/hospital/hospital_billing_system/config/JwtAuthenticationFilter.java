package com.hospital.hospital_billing_system.config;

import com.hospital.hospital_billing_system.admin.entity.Admin;
import com.hospital.hospital_billing_system.admin.entity.User;
import com.hospital.hospital_billing_system.admin.repository.AdminRepository;
import com.hospital.hospital_billing_system.admin.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            AdminRepository adminRepository,
            UserRepository userRepository,
            TokenBlacklistService tokenBlacklistService) {

        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        log.info("========== JWT FILTER ==========");
        log.info("Request URI: {}", requestUri);

        // Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // No token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            log.info("No JWT token found");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            // Check blacklist
            if (tokenBlacklistService.isTokenBlacklisted(token)) {

                log.warn("JWT token is blacklisted");

                SecurityContextHolder.clearContext();

                filterChain.doFilter(request, response);
                return;
            }

            // Extract username
            String username = jwtService.extractUsername(token);

            log.info("JWT Username: {}", username);

            if (username == null) {

                log.warn("Username could not be extracted from JWT");

                filterChain.doFilter(request, response);
                return;
            }

            // Check whether authentication already exists
            if (SecurityContextHolder.getContext().getAuthentication() != null) {

                log.info("Authentication already exists");

                filterChain.doFilter(request, response);
                return;
            }

            // Extract role from token
            String tokenRole = jwtService.extractRole(token);

            log.info("JWT Role: {}", tokenRole);

            // Get role from database
            String databaseRole =
                    getDatabaseRole(username, tokenRole);

            log.info("Database Role: {}", databaseRole);

            // Check account status
            boolean accountActive =
                    databaseRole != null &&
                            isAccountActive(username, databaseRole);

            log.info("Account Active: {}", accountActive);

            // Validate token
            boolean tokenValid =
                    jwtService.isTokenValid(token, username);

            log.info("Token Valid: {}", tokenValid);

            // Authenticate user
            if (databaseRole != null
                    && databaseRole.equals(tokenRole)
                    && accountActive
                    && tokenValid) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + databaseRole
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(authority)
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                log.info(
                        "JWT Authentication SUCCESS: username={}, role={}",
                        username,
                        databaseRole
                );

            } else {

                log.warn(
                        "JWT Authentication FAILED: username={}, tokenRole={}, databaseRole={}, active={}, valid={}",
                        username,
                        tokenRole,
                        databaseRole,
                        accountActive,
                        tokenValid
                );
            }

        } catch (Exception exception) {

            SecurityContextHolder.clearContext();

            log.error(
                    "JWT authentication error: {}",
                    exception.getMessage(),
                    exception
            );
        }

        log.info("================================");

        filterChain.doFilter(request, response);
    }

    private boolean isAccountActive(
            String username,
            String role) {

        if ("ADMIN".equals(role)) {

            return adminRepository
                    .findByUsername(username)
                    .map(Admin::getActive)
                    .orElse(false);
        }

        return userRepository
                .findByUsername(username)
                .map(User::getActive)
                .orElse(false);
    }

    private String getDatabaseRole(
            String username,
            String tokenRole) {

        if ("ADMIN".equals(tokenRole)) {

            return adminRepository
                    .findByUsername(username)
                    .map(admin -> "ADMIN")
                    .orElse(null);
        }

        return userRepository
                .findByUsername(username)
                .map(user -> user.getRole().name())
                .orElse(null);
    }
}