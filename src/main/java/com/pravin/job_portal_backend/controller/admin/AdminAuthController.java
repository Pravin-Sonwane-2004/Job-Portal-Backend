package com.pravin.job_portal_backend.controller.admin;

import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.service.admin.AdminAuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
      this.adminAuthService = adminAuthService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserRegistrationDTO> registerAdmin(@RequestBody UserRegistrationDTO registrationDto) {
        Optional<UserRegistrationDTO> registeredAdmin = adminAuthService.registerAdmin(registrationDto);
        return registeredAdmin
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
