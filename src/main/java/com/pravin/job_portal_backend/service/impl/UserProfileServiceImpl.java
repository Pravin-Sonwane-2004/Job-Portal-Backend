package com.pravin.job_portal_backend.service.impl;

import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.service.interfaces.UserProfileService;
import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.dto.UpdateUserProfile;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.exception.UserNotFoundException;
import com.pravin.job_portal_backend.mapper.UserMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserProfileServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserDto> findByUserName(String email) {
        return repository.findByEmail(email).map(UserMapper::toDto);
    }

    @Override
    @Transactional
    public void updateUserProfile(Long userId, UpdateUserProfile request) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        User currentUser = getAuthenticatedUser();

        boolean isAdmin = Role.ADMIN.equals(currentUser.getRole());
        boolean targetIsAdmin = Role.ADMIN.equals(user.getRole());

        if (targetIsAdmin && !isAdmin) {
            logger.warn("Unauthorized attempt to update admin profile by user: {}", currentUser.getEmail());
            throw new AccessDeniedException("Only admin can update admin profiles.");
        }

        boolean isUpdated = false;

        if (request.getName() != null) {
            user.setName(request.getName());
            isUpdated = true;
            logger.debug("Updated name for userId {}: {}", userId, request.getName());
        }

        if (request.getEmail() != null) {
            if (!isAdmin) {
                throw new AccessDeniedException("Only admin can update email.");
            }

            Optional<User> existingEmailUser = repository.findByEmail(request.getEmail());
            if (existingEmailUser.isPresent() && !existingEmailUser.get().getId().equals(userId)) {
                throw new IllegalArgumentException("Email already in use by another user.");
            }

            user.setEmail(request.getEmail());
            isUpdated = true;
        }

        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
            isUpdated = true;
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
            isUpdated = true;
        }

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
            isUpdated = true;
        }

        if (request.getLinkedinUrl() != null) {
          user.setLinkedinUrl(request.getLinkedinUrl());
          isUpdated = true;
        }
        if (request.getGithubURL() != null) {
          user.setLinkedinUrl(request.getLinkedinUrl());
          isUpdated = true;
        }

        if (request.getSkills() != null) {
            user.setSkills(request.getSkills());
            isUpdated = true;
        }

        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
            isUpdated = true;
        }

        if (request.getDesignation() != null) {
            user.setDesignation(request.getDesignation());
            isUpdated = true;
        }

        if (request.getVerified() != null) {
            user.setVerified(request.getVerified());
            isUpdated = true;
        }
    }

    @Override
    public String getUserNameWithId(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        return user.getName() != null ? user.getName() : "User";
    }

    @Override
    public UserDto getUserProfile(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return UserMapper.toDto(user);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Not authenticated.");
        }

        String currentUserEmail = authentication.getName();
        return repository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found in database"));
    }
}
