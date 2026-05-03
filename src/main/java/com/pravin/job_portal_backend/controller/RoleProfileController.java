package com.pravin.job_portal_backend.controller;

import com.pravin.job_portal_backend.service.interfaces.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role-profile")
public class RoleProfileController {
    private final UserProfileService userProfileService;

    public RoleProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/full-name")
    public ResponseEntity<String> getFullName(Authentication authentication) {
        String username = authentication.getName();
        return userProfileService.findByUserName(username)
                .map(user -> ResponseEntity.ok(user.getName() != null && !user.getName().isBlank() ? user.getName() : user.getEmail()))
                .orElseGet(() -> ResponseEntity.ok(username));
    }
}
