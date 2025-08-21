package com.pravin.job_portal_backend.controller.admin;

import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.mapper.user_mapper.UserAuthMapper;
import com.pravin.job_portal_backend.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * Get user by ID
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserRegistrationDTO> getUserById(@PathVariable Long id) {
        User user = adminService.getUserById(id);
        return ResponseEntity.ok(UserAuthMapper.toRegistrationDto(user));
    }

    /**
     * Get user by Email
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserRegistrationDTO> getUserByEmail(@PathVariable String email) {
        User user = adminService.getUserByEmail(email);
        return ResponseEntity.ok(UserAuthMapper.toRegistrationDto(user));
    }

    /**
     * Update user role
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/{id}/role")
    public ResponseEntity<UserRegistrationDTO> updateUserRole(
            @PathVariable Long id,
            @RequestParam Role role) {
        User user = adminService.updateUserRole(id, role);
        return ResponseEntity.ok(UserAuthMapper.toRegistrationDto(user));
    }

    /**
     * Block / Unblock User
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user/{id}/block")
    public ResponseEntity<UserRegistrationDTO> toggleBlockUser(
            @PathVariable Long id,
            @RequestParam boolean block) {
        User user = adminService.toggleBlockUser(id, block);
        return ResponseEntity.ok(UserAuthMapper.toRegistrationDto(user));
    }
}
