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
    import lombok.RequiredArgsConstructor;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.util.Collections;
    import java.util.Optional;

    @Service
    @RequiredArgsConstructor
    public class UserAuthServiceImpl implements UserAuthService {

        private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final EmailService emailService;
        private final JwtUtil jwtUtil;

        /**
         * Register a new USER or RECRUITER.
         * ADMIN cannot be self-registered.
         */
        @Override
        @Transactional
        public Optional<UserRegistrationDTO> register(UserRegistrationDTO registrationDto) {
            User user = UserAuthMapper.toRegistrationEntity(registrationDto);

            // Email already exists?
            if (repository.findByEmail(user.getEmail()).isPresent()) {
                logger.warn("Attempt to register with existing email: {}", user.getEmail());
                throw new IllegalArgumentException("Email already registered.");
            }

            // Prevent ADMIN registration
            if (user.getRole() == Role.ADMIN) {
                logger.error("Unauthorized attempt to self-register with ADMIN role: {}", user.getEmail());
                throw new IllegalArgumentException("Cannot self-register as ADMIN.");
            }

            // Assign default role if null
            assignRole(user);

            // Encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save user
            User savedUser = repository.saveAndFlush(user);
            logger.info("User {} registered with role {} and ID {}",
                    savedUser.getEmail(), savedUser.getRole(), savedUser.getId());

            // Send welcome email (non-blocking)
            try {
                emailService.sendRegistrationWelcomeEmail(savedUser.getEmail());
            } catch (Exception e) {
                logger.error("Failed to send welcome email to {}", savedUser.getEmail(), e);
            }

            return Optional.of(UserAuthMapper.toRegistrationDto(savedUser));
        }

        /**
         * Login for USER, RECRUITER, ADMIN
         */
        @Override
        public String loginAndGenerateToken(UserLoginDTO toLoginDto) {
            User user = repository.findByEmail(toLoginDto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

            if (!passwordEncoder.matches(toLoginDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid email or password");
            }

            // Convert role to authority
            String roleName = "ROLE_" + user.getRole().name();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

            // Generate JWT
            return jwtUtil.generateToken(
                    user.getEmail(),
                    user.getId(),
                    Collections.singleton(authority));
        }

        /**
         * Ensure correct role assignment on registration.
         */
  private void assignRole(User user) {
    if (user.getRole() == null) {
        user.setRole(Role.USER);
        logger.info("No role provided. Defaulting to USER.");
    } else if (user.getRole() != Role.USER && user.getRole() != Role.RECRUITER) {
        throw new IllegalArgumentException("Unsupported role: " + user.getRole());
    }
        }
    }
