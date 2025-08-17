package com.pravin.job_portal_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/** Standard error payload for API responses. */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    /** Optional map of per-field errors for validation issues. */
    private Map<String, String> errors;
}
