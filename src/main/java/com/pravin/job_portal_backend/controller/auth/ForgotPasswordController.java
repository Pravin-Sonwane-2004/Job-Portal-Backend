package com.pravin.job_portal_backend.controller.auth;

import com.pravin.job_portal_backend.service.authService.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserAuthService userAuthService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestParam String email) {
        String token = userAuthService.forgotPassword(email);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Password reset link sent to email"
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        userAuthService.resetPassword(token, newPassword);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Password reset successful"
        ));
    }
}
