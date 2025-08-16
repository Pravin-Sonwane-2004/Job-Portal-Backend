package com.pravin.job_portal_backend.controller.admin;

import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.service.admin.AdminAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/admin")
public class AdminController {
    private final AdminAuthService adminAuthService;

    public AdminController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/new")
    public ResponseEntity<Optional<UserRegistrationDTO>> signup(@Valid @RequestBody UserRegistrationDTO registrationDto) {
        return ResponseEntity.ok(adminAuthService.registerAdmin(registrationDto));
    }
}
