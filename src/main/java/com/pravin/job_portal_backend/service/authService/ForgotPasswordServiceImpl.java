package com.pravin.job_portal_backend.service.authService;

import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.email.EmailService;
import com.pravin.job_portal_backend.utilis.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public ForgotPasswordServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Generate JWT reset token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), Collections.emptyList());

        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
        logger.info("Generated reset token: {}", token);
        logger.info("Password reset link: {}", resetLink);

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        } catch (Exception e) {
            logger.error("Error sending password reset email: {}", e.getMessage());
        }

        return "Password reset link has been sent to your email.";
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        logger.info("Resetting password with token: {}", token);

        // 1. Check token validity
        if (jwtUtil.isTokenExpired(token)) {
            throw new IllegalArgumentException("Reset token has expired. Please request a new one.");
        }

        // 2. Extract email from token
        String email = jwtUtil.extractUsername(token);

        // 3. Get user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reset token. User not found."));

        // 4. Encode and update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        logger.info("Password updated successfully for {}", user.getEmail());
    }
}
