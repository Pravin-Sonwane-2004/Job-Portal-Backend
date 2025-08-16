package com.pravin.job_portal_backend.mapper.user_mapper;

import com.pravin.job_portal_backend.dto.user_dtos.UserDto;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

    public class UserMapper {

        private UserMapper() {
        }

        public static UserDto toDto(User user) {
            if (user == null) return null;

            return UserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .avatarUrl(user.getAvatarUrl())
                    .designation(user.getDesignation())
                    .verified(user.getVerified())
                    .location(user.getLocation())
                    .bio(user.getBio())
                    .phoneNumber(user.getPhoneNumber())
                    .linkedinUrl(user.getLinkedinUrl())
//                    .jobRole(user.getJobRole())
                    .experienceLevel(user.getExperienceLevel())
                    .role(user.getRole())
                    .skills(user.getSkills() != null ? List.copyOf(user.getSkills()) : null)
                    .build();
        }

        public static User toEntity(UserDto dto) {
            if (dto == null) return null;

            return User.builder()
                    .id(dto.getId())
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .avatarUrl(dto.getAvatarUrl())
                    .designation(dto.getDesignation())
                    .verified(dto.getVerified())
                    .location(dto.getLocation())
                    .bio(dto.getBio())
                    .phoneNumber(dto.getPhoneNumber())
                    .linkedinUrl(dto.getLinkedinUrl())
//                    .jobRole(dto.getJobRole())
                    .experienceLevel(dto.getExperienceLevel())
                    .role(dto.getRole())
                    .skills(dto.getSkills() != null ? List.copyOf(dto.getSkills()) : null)
                    .build();
        }
    }
