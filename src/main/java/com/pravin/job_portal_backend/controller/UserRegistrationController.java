package com.pravin.job_portal_backend.controller;

import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.service.interfaces.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/register")
public class UserRegistrationController {
    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody UserLoginDTO userLoginDTO) {
        Optional<UserDto> user = userRegistrationService.saveUser(userLoginDTO);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        Optional<UserDto> user = userRegistrationService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
