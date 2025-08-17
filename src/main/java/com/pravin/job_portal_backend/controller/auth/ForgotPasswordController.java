package com.pravin.job_portal_backend.controller.auth;

import com.pravin.job_portal_backend.service.authService.ForgotPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    /**
     * Step 1: Forgot Password → Generate reset token and send email
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String response = forgotPasswordService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Step 2: Reset Password → Validate token and update password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        forgotPasswordService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
