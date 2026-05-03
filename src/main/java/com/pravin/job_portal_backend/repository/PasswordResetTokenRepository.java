package com.pravin.job_portal_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.PasswordResetToken;
import com.pravin.job_portal_backend.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
  Optional<PasswordResetToken> findByToken(String token);

  void deleteByUser(User user);
}
