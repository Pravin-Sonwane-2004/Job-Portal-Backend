package com.pravin.job_portal_backend.mapper.user_mapper;

import com.pravin.job_portal_backend.dto.user_dtos.UserLoginDTO;
import com.pravin.job_portal_backend.entity.User;

public final class UserLoginMapper {

    private UserLoginMapper () {

    }

    public static UserLoginDTO toDto(User user) {
        if (user == null) return null;

        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    // === DTO → Entity ===

        public static User toEntity(UserLoginDTO dto) {
            if (dto == null) return null;

            User user = new User();
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setRole(dto.getRole());
            return user;
        }
}
