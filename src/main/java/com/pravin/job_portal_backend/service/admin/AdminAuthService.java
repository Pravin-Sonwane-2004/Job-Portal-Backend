package com.pravin.job_portal_backend.service.admin;

import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;

import java.util.Optional;

public interface AdminAuthService {
    Optional<UserRegistrationDTO> registerAdmin(UserRegistrationDTO registrationDto);
}
