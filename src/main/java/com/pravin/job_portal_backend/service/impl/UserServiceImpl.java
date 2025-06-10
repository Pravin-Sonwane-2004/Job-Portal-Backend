package com.pravin.job_portal_backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.dto.UpdateUserProfile;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.dto.UserRegistrationDTO;
import com.pravin.job_portal_backend.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserDto registerUser(UserRegistrationDTO registrationDTO) {
        // Implement registration logic
        return null;
    }

    @Override
    public UserDto loginUser(UserLoginDTO loginDTO) {
        // Implement login logic
        return null;
    }

    @Override
    public UserDto updateUserProfile(Long userId, UpdateUserProfile updateDTO) {
        // Implement update logic
        return null;
    }

    @Override
    public UserDto getUserById(Long userId) {
        // Implement fetch logic
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        // Implement fetch all logic
        return null;
    }

    @Override
    public void deleteUser(Long userId) {
        // Implement delete logic
    }
}
