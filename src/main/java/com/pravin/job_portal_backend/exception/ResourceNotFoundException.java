package com.pravin.job_portal_backend.exception;

/** Generic reusable not-found exception for services. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
