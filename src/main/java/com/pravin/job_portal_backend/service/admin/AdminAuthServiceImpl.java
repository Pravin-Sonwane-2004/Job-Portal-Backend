package com.pravin.job_portal_backend.service.admin;

import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.exception.UnauthorizedRoleAssignmentException;
import com.pravin.job_portal_backend.mapper.user_mapper.UserAuthMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AdminAuthServiceImpl.class);
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder; // use interface

    public AdminAuthServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Optional<UserRegistrationDTO> registerAdmin(UserRegistrationDTO registrationDto) {
        User user = UserAuthMapper.toRegistrationEntity(registrationDto);

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("Attempt to register admin with existing email: {}", user.getEmail());
            throw new UnauthorizedRoleAssignmentException("Email already registered.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);
        logger.info("Admin role assigned to user: {}", user.getEmail());

        User savedAdmin = repository.saveAndFlush(user);
        logger.info("Admin {} saved with ID: {}", savedAdmin.getEmail(), savedAdmin.getId());

        return Optional.of(UserAuthMapper.toRegistrationDto(savedAdmin));
    }
}
