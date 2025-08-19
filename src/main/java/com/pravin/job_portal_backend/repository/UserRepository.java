package com.pravin.job_portal_backend.repository;

import com.pravin.job_portal_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  void deleteByEmail(String email);
//  void findByResetToken(String token);

    Optional<Object> findByResetToken(String token);
    // Optional<JobApplication> findByUsername(String email);
}
