package com.pravin.job_portal_backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.ForgotPasswordRequest;
import com.pravin.job_portal_backend.dto.ResetPasswordRequest;
import com.pravin.job_portal_backend.service.interfaces.PasswordResetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/public/password")
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  public PasswordResetController(PasswordResetService passwordResetService) {
    this.passwordResetService = passwordResetService;
  }

  @PostMapping("/forgot")
  public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    passwordResetService.requestPasswordReset(request.email());
    return ResponseEntity.ok(Map.of("message", "If this email exists, a password reset link has been sent."));
  }

  @PostMapping("/reset")
  public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    passwordResetService.resetPassword(request.token(), request.newPassword());
    return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
  }
}
