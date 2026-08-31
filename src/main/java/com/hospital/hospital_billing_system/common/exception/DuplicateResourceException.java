package com.hospital.hospital_billing_system.common.exception;

public class DuplicateResourceException extends RuntimeException {

    // create exception with message
    public DuplicateResourceException(String message) {
        super(message);
    }
}