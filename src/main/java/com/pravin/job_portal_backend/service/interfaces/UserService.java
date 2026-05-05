package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.dto.UserRegistrationDTO;
import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.dto.UpdateUserProfile;
import java.util.List;

public interface UserService {
    UserDto registerUser(UserRegistrationDTO registrationDTO);
    UserDto loginUser(UserLoginDTO loginDTO);
    UserDto updateUserProfile(Long userId, UpdateUserProfile updateDTO);
    UserDto getUserById(Long userId);
    List<UserDto> getAllUsers();
    List<UserDto> searchTalent(String q, String skill, String location, String experienceLevel);
    UserDto getTalentProfile(Long userId);
    void deleteUser(Long userId);
}
