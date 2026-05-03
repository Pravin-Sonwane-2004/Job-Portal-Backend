package com.pravin.job_portal_backend.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.job_portal_backend.entity.EmailRequest;
import com.pravin.job_portal_backend.entity.PasswordResetToken;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.PasswordResetTokenRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.EmailService;
import com.pravin.job_portal_backend.service.interfaces.PasswordResetService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

  private final UserRepository userRepository;
  private final PasswordResetTokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final String frontendUrl;
  private final long tokenExpiryMinutes;

  public PasswordResetServiceImpl(
      UserRepository userRepository,
      PasswordResetTokenRepository tokenRepository,
      PasswordEncoder passwordEncoder,
      EmailService emailService,
      @Value("${app.frontend-url:${FRONTEND_URL:http://localhost:3000}}") String frontendUrl,
      @Value("${app.password-reset.expiry-minutes:${PASSWORD_RESET_EXPIRY_MINUTES:30}}") long tokenExpiryMinutes) {
    this.userRepository = userRepository;
    this.tokenRepository = tokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.frontendUrl = frontendUrl;
    this.tokenExpiryMinutes = tokenExpiryMinutes;
  }

  @Override
  @Transactional
  public void requestPasswordReset(String email) {
    userRepository.findByEmail(email).ifPresent(user -> {
      tokenRepository.deleteByUser(user);
      String token = UUID.randomUUID().toString() + UUID.randomUUID();
      PasswordResetToken resetToken = PasswordResetToken.builder()
          .user(user)
          .token(token)
          .createdAt(LocalDateTime.now())
          .expiresAt(LocalDateTime.now().plusMinutes(tokenExpiryMinutes))
          .build();
      tokenRepository.save(resetToken);
      sendResetEmail(user, token);
    });
  }

  @Override
  @Transactional
  public void resetPassword(String token, String newPassword) {
    PasswordResetToken resetToken = tokenRepository.findByToken(token)
        .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token."));

    if (resetToken.isUsed() || resetToken.isExpired()) {
      throw new IllegalArgumentException("Invalid or expired reset token.");
    }

    User user = resetToken.getUser();
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    resetToken.setUsedAt(LocalDateTime.now());
    tokenRepository.save(resetToken);
  }

  private void sendResetEmail(User user, String token) {
    String resetLink = frontendUrl.replaceAll("/$", "") + "/reset-password?token=" + token;
    String body = "Hello " + displayName(user) + ",\n\n"
        + "We received a request to reset your Job Portal password.\n\n"
        + "Reset your password here: " + resetLink + "\n\n"
        + "This link expires in " + tokenExpiryMinutes + " minutes. If you did not request this, ignore this email.";

    try {
      emailService.sendEmail(new EmailRequest(user.getEmail(), "Reset your Job Portal password", body));
    } catch (Exception ex) {
      log.warn("Password reset email could not be sent to {}: {}", user.getEmail(), ex.getMessage());
    }
  }

  private String displayName(User user) {
    return user.getName() == null || user.getName().isBlank() ? "user" : user.getName();
  }
}
