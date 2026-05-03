package com.pravin.job_portal_backend.service.impl;

import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.exception.UnauthorizedRoleAssignmentException;
import com.pravin.job_portal_backend.mapper.UserMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserDto> saveUser(UserLoginDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        user.setId(null); // Ensure new user

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("Attempt to register with existing email: {}", user.getEmail());
            throw new IllegalArgumentException("Email already in use.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        long userCount = repository.count();

        if (userCount == 0) {
            user.setRole(Role.ADMIN);
            logger.info("First user registered. Assigned ADMIN role to user: {}", user.getEmail());
        } else {
            if (user.getRole() == Role.ADMIN && !isAuthenticatedAdmin()) {
                logger.warn("Unauthorized attempt to assign ADMIN role to user: {}", user.getEmail());
                throw new UnauthorizedRoleAssignmentException("Cannot assign ADMIN role without authorization.");
            }
            user.setRole(Optional.ofNullable(user.getRole()).orElse(Role.USER));
            logger.info("Default USER role assigned to user: {}", user.getEmail());
        }

        User savedUser = repository.saveAndFlush(user);
        logger.info("User {} saved with ID: {}", savedUser.getEmail(), savedUser.getId());

        return Optional.of(UserMapper.toDto(savedUser));
    }

    private boolean isAuthenticatedAdmin() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        logger.info("Updating user with ID: {}", user.getId());
        // Ensure password is encoded
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) { // Avoid double encoding
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User updatedUser = repository.save(user);
        logger.info("User updated: {}", updatedUser.getEmail());
        return UserMapper.toDto(updatedUser);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return repository.findByEmail(email).map(UserMapper::toDto);
    }
}
