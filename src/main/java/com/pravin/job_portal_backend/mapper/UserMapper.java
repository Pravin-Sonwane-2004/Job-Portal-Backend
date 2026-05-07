package com.pravin.job_portal_backend.mapper;

import java.util.stream.Collectors;

import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.entity.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setDesignation(user.getDesignation());
        dto.setVerified(user.getVerified());
        dto.setLocation(user.getLocation());
        dto.setBio(user.getBio());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setLinkedinUrl(user.getLinkedinUrl());
        dto.setJobRole(user.getJobRole());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);
        dto.setExperienceLevel(user.getExperienceLevel());
        dto.setSkills(user.getSkills() != null ? user.getSkills().stream().collect(Collectors.toList()) : null);
        dto.setCompanyId(user.getCompany() != null ? user.getCompany().getId() : null);
        dto.setCompanyName(user.getCompany() != null ? user.getCompany().getName() : null);
        return dto;
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setDesignation(dto.getDesignation());
        user.setVerified(dto.getVerified());
        user.setLocation(dto.getLocation());
        user.setBio(dto.getBio());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setJobRole(dto.getJobRole());
        user.setExperienceLevel(dto.getExperienceLevel());
        // Role and skills should be set in service if needed
        return user;
    }

    public static User toEntity(UserLoginDTO userDTO) {
        if (userDTO == null) return null;
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole()); // If role is an enum, you may need to convert
        // Set other fields if present in UserLoginDTO
        return user;
    }
}
