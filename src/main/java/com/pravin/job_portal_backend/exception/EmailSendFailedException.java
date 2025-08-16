package com.pravin.job_portal_backend.exception;

public class EmailSendFailedException extends RuntimeException {
    public EmailSendFailedException(String message, Exception e) {
        super(message);
    }
}