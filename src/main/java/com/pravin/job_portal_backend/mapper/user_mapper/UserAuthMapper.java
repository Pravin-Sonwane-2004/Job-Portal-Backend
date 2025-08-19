package com.pravin.job_portal_backend.mapper.user_mapper;

import com.pravin.job_portal_backend.dto.user_dtos.UserLoginDTO;
import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;

public final class UserAuthMapper {

    private UserAuthMapper() {
    }

    // === User → Login DTO ===
    public static UserLoginDTO toLoginDto(User user) {
        if (user == null) return null;

        UserLoginDTO dto = new UserLoginDTO();
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    // === Login DTO → User ===
    public static User toUserLoginEntity(UserLoginDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }

    // === User → Registration DTO ===
    public static UserRegistrationDTO toRegistrationDto(User user) {
        if (user == null) return null;

        UserRegistrationDTO dto = new UserRegistrationDTO();
       dto.setName(user.getName());

        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    // === Registration DTO → User ===
    public static User toRegistrationEntity(UserRegistrationDTO dto) {
        if (dto == null)
            return null;

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }
}
