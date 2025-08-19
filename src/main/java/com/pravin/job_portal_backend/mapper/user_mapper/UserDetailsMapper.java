package com.pravin.job_portal_backend.mapper.user_mapper;

import com.pravin.job_portal_backend.dto.user_dtos.UserDetailsDto;
import com.pravin.job_portal_backend.entity.User;

public class UserDetailsMapper {

    public static UserDetailsDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDetailsDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                // .username(user.getUsername())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .designation(user.getDesignation())
                .verified(user.getVerified())
                .location(user.getLocation())
                .bio(user.getBio())
                .phoneNumber(user.getPhoneNumber())
                .linkedinUrl(user.getLinkedinUrl())
                .githubUrl(user.getGithubUrl())
                .blocked(user.isBlocked())
                .role(user.getRole())
                .experienceLevel(user.getExperienceLevel())
                .status(user.getStatus())
                .skills(user.getSkills())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static User toEntity(UserDetailsDto dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                // .username(dto.getUsername())
                .name(dto.getName())
                .avatarUrl(dto.getAvatarUrl())
                .designation(dto.getDesignation())
                .verified(dto.getVerified())
                .location(dto.getLocation())
                .bio(dto.getBio())
                .phoneNumber(dto.getPhoneNumber())
                .linkedinUrl(dto.getLinkedinUrl())
                .githubUrl(dto.getGithubUrl())
                .blocked(dto.isBlocked())
                .role(dto.getRole())
                .experienceLevel(dto.getExperienceLevel())
                .status(dto.getStatus())
                .skills(dto.getSkills())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
