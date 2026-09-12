package com.hospital.hospital_billing_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Disable CSRF for REST APIs
                .csrf(AbstractHttpConfigurer::disable)

                // Disable default login and basic authentication
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // Configure authentication and authorization
                .authorizeHttpRequests(auth -> auth

                        // Login APIs are public
                        .requestMatchers(
                                "/api/auth/admin/login",
                                "/api/auth/user/login"
                        ).permitAll()

                        // Admin APIs require ADMIN JWT
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // All other APIs require a valid JWT
                        .anyRequest().authenticated()
                )

                // Check JWT before Spring Security username/password authentication
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}