package com.hospital.hospital_billing_system.common.exception;

import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    // handle resource not found exception

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            HttpServletRequest request) {

        log.warn(
                "Resource not found. Method: {}, URI: {}, Message: {}",
                request.getMethod(),
                request.getRequestURI(),
                exception.getMessage()
        );

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        log.warn(
                "Duplicate resource. Method: {}, URI: {}, Message: {}",
                request.getMethod(),
                request.getRequestURI(),
                ex.getMessage()
        );

        ErrorResponse errorResponse1 = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse1);
    }
}