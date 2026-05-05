package com.pravin.job_portal_backend.controller;

import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.ForgotPasswordRequest;
import com.pravin.job_portal_backend.dto.PasswordResetResult;
import com.pravin.job_portal_backend.dto.ResetPasswordRequest;
import com.pravin.job_portal_backend.service.interfaces.PasswordResetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/public/password")
public class PasswordResetController {

  private final PasswordResetService passwordResetService;
  private final Environment environment;

  public PasswordResetController(PasswordResetService passwordResetService, Environment environment) {
    this.passwordResetService = passwordResetService;
    this.environment = environment;
  }

  @PostMapping("/forgot")
  public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    PasswordResetResult result = passwordResetService.requestPasswordReset(request.email());
    String message = "If this email exists, a password reset link has been sent.";

    if (isNonProd() && result.userFound()) {
      if (result.emailSent()) {
        return ResponseEntity.ok(Map.of("message", message, "resetLink", result.resetLink()));
      }
      return ResponseEntity.ok(Map.of(
          "message", "Reset link was created, but email could not be sent. Check SMTP settings.",
          "resetLink", result.resetLink()));
    }

    return ResponseEntity.ok(Map.of("message", message));
  }

  @PostMapping("/reset")
  public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    passwordResetService.resetPassword(request.token(), request.newPassword());
    return ResponseEntity.ok(Map.of("message", "Password reset successfully."));
  }

  private boolean isNonProd() {
    return !environment.acceptsProfiles(Profiles.of("prod"));
  }
}
