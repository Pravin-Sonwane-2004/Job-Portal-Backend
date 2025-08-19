package com.pravin.job_portal_backend.service.admin;

import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

public interface AdminService {

    User getUserById(Long userId);

    User getUserByEmail(String email);

    User updateUserRole(Long userId, Role newRole);

    User toggleBlockUser(Long userId, boolean block);

    List<UserDetails> getAll();

    void deleteByUsername(String email);

    Optional<UserRegistrationDTO> registerAdmin(UserRegistrationDTO registrationDto);
}
