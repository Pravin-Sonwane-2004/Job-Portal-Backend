package com.pravin.job_portal_backend.service.authService;

import com.pravin.job_portal_backend.dto.user_dtos.UserLoginDTO;
import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;

import java.util.Optional;

public interface UserAuthService {
    
    //registration
    Optional<UserRegistrationDTO> register(UserRegistrationDTO registrationDto);
    //login
    String loginAndGenerateToken(UserLoginDTO toLoginDto);

}
