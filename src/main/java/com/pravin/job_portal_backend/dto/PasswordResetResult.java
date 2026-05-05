package com.pravin.job_portal_backend.dto;

//PasswordResetResult result =
//        new PasswordResetResult(true, true, "http://localhost:8080/reset?token=abc");
public record PasswordResetResult(
    boolean userFound,
    boolean emailSent,
    String resetLink) {
}
