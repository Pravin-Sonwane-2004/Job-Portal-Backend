package com.pravin.job_portal_backend.config;

import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
public class AdminBootstrapConfig implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(AdminBootstrapConfig.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.admin.email:}")
  private String adminEmail;

  @Value("${app.admin.password:}")
  private String adminPassword;

  public AdminBootstrapConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void run(String... args) {
    long adminCount = userRepository.countByRole(Role.ADMIN);
    if (adminCount > 1) {
      log.error("Multiple ADMIN users already exist. Please keep only one admin account.");
      return;
    }
    if (adminCount == 1) {
      return;
    }

    if (!StringUtils.hasText(adminEmail) || !StringUtils.hasText(adminPassword)) {
      log.warn("No ADMIN user exists. Configure app.admin.email and app.admin.password to bootstrap one.");
      return;
    }

    User admin = userRepository.findByEmail(adminEmail.trim())
        .orElseGet(User::new);

    admin.setEmail(adminEmail.trim());
    admin.setPassword(passwordEncoder.encode(adminPassword));
    admin.setRole(Role.ADMIN);
    if (!StringUtils.hasText(admin.getName())) {
      admin.setName("Admin");
    }

    User savedAdmin = userRepository.saveAndFlush(admin);
    log.info("Bootstrapped ADMIN user with ID: {}", savedAdmin.getId());
  }
}
