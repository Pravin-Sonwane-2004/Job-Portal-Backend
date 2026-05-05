package com.pravin.job_portal_backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.job_portal_backend.dto.UpdateUserProfile;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.dto.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.mapper.UserMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole() != null ? registrationDTO.getRole() : Role.USER);
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto loginUser(UserLoginDTO loginDTO) {
        return userRepository.findByEmail(loginDTO.getEmail())
                .filter(user -> passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
                .map(UserMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public UserDto updateUserProfile(Long userId, UpdateUserProfile updateDTO) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        if (updateDTO.getName() != null) user.setName(updateDTO.getName());
        if (updateDTO.getEmail() != null) user.setEmail(updateDTO.getEmail());
        if (updateDTO.getLocation() != null) user.setLocation(updateDTO.getLocation());
        if (updateDTO.getBio() != null) user.setBio(updateDTO.getBio());
        if (updateDTO.getPhoneNumber() != null) user.setPhoneNumber(updateDTO.getPhoneNumber());
        if (updateDTO.getLinkedinUrl() != null) user.setLinkedinUrl(updateDTO.getLinkedinUrl());
        if (updateDTO.getGithubURL() != null) user.setGithubURL(updateDTO.getGithubURL());
        if (updateDTO.getSkills() != null) user.setSkills(updateDTO.getSkills());
        if (updateDTO.getAvatarUrl() != null) user.setAvatarUrl(updateDTO.getAvatarUrl());
        if (updateDTO.getDesignation() != null) user.setDesignation(updateDTO.getDesignation());
        if (updateDTO.getJobRole() != null) user.setJobRole(updateDTO.getJobRole());
        if (updateDTO.getExperienceLevel() != null) user.setExperienceLevel(updateDTO.getExperienceLevel());
        if (updateDTO.getVerified() != null) user.setVerified(updateDTO.getVerified());
        if (updateDTO.getRole() != null) user.setRole(updateDTO.getRole());

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId).map(UserMapper::toDto).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> searchTalent(String q, String skill, String location, String experienceLevel) {
        String query = lower(q);
        String skillQuery = lower(skill);
        String locationQuery = lower(location);
        String experienceQuery = lower(experienceLevel);

        return userRepository.findByRole(Role.USER).stream()
                .filter(user -> query == null
                        || contains(user.getName(), query)
                        || contains(user.getEmail(), query)
                        || contains(user.getDesignation(), query)
                        || contains(user.getJobRole(), query))
                .filter(user -> skillQuery == null || (user.getSkills() != null && user.getSkills().stream()
                        .anyMatch(value -> contains(value, skillQuery))))
                .filter(user -> locationQuery == null || contains(user.getLocation(), locationQuery))
                .filter(user -> experienceQuery == null || (user.getExperienceLevel() != null
                        && user.getExperienceLevel().name().toLowerCase().contains(experienceQuery)))
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getTalentProfile(Long userId) {
        return userRepository.findById(userId)
                .filter(user -> Role.USER.equals(user.getRole()))
                .map(UserMapper::toDto)
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    private String lower(String value) {
        return value == null || value.isBlank() ? null : value.toLowerCase();
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }
}
