package com.pravin.job_portal_backend.service.authService;

import com.pravin.job_portal_backend.dto.user_dtos.UserLoginDTO;
import com.pravin.job_portal_backend.dto.user_dtos.UserRegistrationDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.mapper.user_mapper.UserAuthMapper;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.email.EmailService;
import com.pravin.job_portal_backend.utilis.JwtUtil;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public UserAuthServiceImpl(UserRepository repository,
                               PasswordEncoder passwordEncoder,
                               JwtUtil jwtUtil,
                               EmailService emailService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    /**
     * Register a new user and send welcome email
     */
    @Override
    @Transactional
    public Optional<UserRegistrationDTO> register(UserRegistrationDTO registrationDto) {
        User user = UserAuthMapper.toRegistrationEntity(registrationDto);

        // Check if email already exists
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            logger.warn("Attempt to register with existing email: {}", user.getEmail());
            throw new IllegalArgumentException("Email already registered.");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign default role if not set
        assignRole(user);

        // Save user
        User savedUser = repository.saveAndFlush(user);
        logger.info("User {} saved with ID: {}", savedUser.getEmail(), savedUser.getId());

        // Send welcome email (does not break registration if fails)
        try {
            emailService.sendRegistrationWelcomeEmail(savedUser.getEmail());
            logger.info("Welcome email sent to {}", savedUser.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}", savedUser.getEmail(), e);
        }

        return Optional.of(UserAuthMapper.toRegistrationDto(savedUser));
    }

    /**
     * Login and generate JWT token
     */
    @Override
    public String loginAndGenerateToken(UserLoginDTO toLoginDto) {
        User user = repository.findByEmail(toLoginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(toLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Convert enum Role -> String for GrantedAuthority
        String roleName = "ROLE_" + user.getRole().name(); // e.g. ROLE_USER, ROLE_ADMIN
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        // Generate token with email, userId, and role
        return jwtUtil.generateToken(
                user.getEmail(),
                user.getId(),
                Collections.singleton(authority)
        );
    }


    /**
     * Send forgot password link
     */
    @Override
    public String forgotPassword(String email) {
        // Normally generate token + send email with reset link
        logger.info("Forgot password requested for {}", email);
        return "Password reset link sent to " + email;
    }

    /**
     * Reset password using token
     */
    @Override
    public void resetPassword(String token, String newPassword) {
        logger.info("Reset password with token: {}", token);
        repository.findByEmail(jwtUtil.extractUsername(token)).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.save(user);
            logger.info("Password updated for {}", user.getEmail());
        });
    }

    /**
     * Assign default role
     */
    private void assignRole(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.valueOf("USER")); // default role
        }
    }
}
