package com.pravin.job_portal_backend.service.authService;

import com.pravin.job_portal_backend.dto.user_dtos.UserLoginDTO;
import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;

import java.util.Optional;

public interface UserAuthService {
    Optional<UserRegistrationDTO> register(UserRegistrationDTO registrationDto);
    String loginAndGenerateToken(UserLoginDTO toLoginDto);
//    void assignRole(User user);
    String forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}