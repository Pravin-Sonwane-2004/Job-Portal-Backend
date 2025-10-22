package com.pravin.job_portal_backend.controller.auth;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pravin.job_portal_backend.dto.user_dtos.UserLoginDTO;
import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.service.authService.UserAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<Optional<UserRegistrationDTO>> signup(
            @Valid @RequestBody UserRegistrationDTO registrationDto) {
        return ResponseEntity.ok(userAuthService.register(registrationDto));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO loginDto) {
        String token = userAuthService.loginAndGenerateToken(loginDto);
        return ResponseEntity.ok(Map.of("token", token));
    }

}
