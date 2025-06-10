package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.dto.UserDto;
import java.util.Optional;

public interface UserRegistrationService {
    Optional<UserDto> saveUser(UserLoginDTO userDTO);
    UserDto updateUser(UserDto userDto);
    Optional<UserDto> findByEmail(String email);
}
