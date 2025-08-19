// package com.pravin.job_portal_backend.service.profile;

// import java.util.Optional;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import
// org.springframework.security.core.JOB_SEEKERdetails.USERNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import
// com.pravin.job_portal_backend.dto.JOB_SEEKER_dtos.JOB_SEEKERDetialsDto;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.enums.Role;
// import com.pravin.job_portal_backend.exception.JOB_SEEKERNotFoundException;
// import com.pravin.job_portal_backend.repository.JOB_SEEKERRepository;

// @Service
// public class JOB_SEEKERProfileServiceImpl implements JOB_SEEKERProfileService
// {

// private static final Logger logger =
// LoggerFactory.getLogger(JOB_SEEKERProfileServiceImpl.class);

// private final JOB_SEEKERRepository repository;

// @Autowired
// private PasswordEncoder passwordEncoder;

// public JOB_SEEKERProfileServiceImpl(JOB_SEEKERRepository repository) {
// this.repository = repository;
// }

// @Override
// public Optional<JOB_SEEKERDetialsDto> findByUSER(String email) {
// return repository.findByEmail(email).map(JOB_SEEKERMapper::toDto);
// }

// @Override
// @Transactional
// public void updateJOB_SEEKERProfile(Long JOB_SEEKERId,
// UpdateJOB_SEEKERProfile request) {
// JOB_SEEKER JOB_SEEKER = repository.findById(JOB_SEEKERId)
// .orElseThrow(() -> new JOB_SEEKERNotFoundException("JOB_SEEKER not found with
// ID: " + JOB_SEEKERId));

// JOB_SEEKER currentJOB_SEEKER = getAuthenticatedJOB_SEEKER();

// boolean isAdmin = Role.ADMIN.equals(currentJOB_SEEKER.getRole());
// boolean targetIsAdmin = Role.ADMIN.equals(JOB_SEEKER.getRole());

// if (targetIsAdmin && !isAdmin) {
// logger.warn("Unauthorized attempt to update admin profile by JOB_SEEKER: {}",
// currentJOB_SEEKER.getEmail());
// throw new AccessDeniedException("Only admin can update admin profiles.");
// }

// boolean isUpdated = false;

// if (request.getName() != null) {
// JOB_SEEKER.setName(request.getName());
// isUpdated = true;
// logger.debug("Updated name for JOB_SEEKERId {}: {}", JOB_SEEKERId,
// request.getName());
// }

// if (request.getEmail() != null) {
// if (!isAdmin) {
// throw new AccessDeniedException("Only admin can update email.");
// }

// Optional<JOB_SEEKER> existingEmailJOB_SEEKER =
// repository.findByEmail(request.getEmail());
// if (existingEmailJOB_SEEKER.isPresent() &&
// !existingEmailJOB_SEEKER.get().getId().equals(JOB_SEEKERId)) {
// throw new IllegalArgumentException("Email already in use by another
// JOB_SEEKER.");
// }

// JOB_SEEKER.setEmail(request.getEmail());
// isUpdated = true;
// }

// if (request.getLocation() != null) {
// JOB_SEEKER.setLocation(request.getLocation());
// isUpdated = true;
// }

// if (request.getBio() != null) {
// JOB_SEEKER.setBio(request.getBio());
// isUpdated = true;
// }

// if (request.getPhoneNumber() != null) {
// JOB_SEEKER.setPhoneNumber(request.getPhoneNumber());
// isUpdated = true;
// }

// if (request.getLinkedinUrl() != null) {
// JOB_SEEKER.setLinkedinUrl(request.getLinkedinUrl());
// isUpdated = true;
// }
// if (request.getGithubURL() != null) {
// JOB_SEEKER.setGithubUrl(request.getGithubURL());
// isUpdated = true;
// }

// if (request.getSkills() != null) {
// JOB_SEEKER.setSkills(request.getSkills());
// isUpdated = true;
// }

// if (request.getAvatarUrl() != null) {
// JOB_SEEKER.setAvatarUrl(request.getAvatarUrl());
// isUpdated = true;
// }

// if (request.getDesignation() != null) {
// JOB_SEEKER.setDesignation(request.getDesignation());
// isUpdated = true;
// }

// if (request.getVerified() != null) {
// JOB_SEEKER.setVerified(request.getVerified());
// isUpdated = true;
// }
// }

// @Override
// public String getUSERWithId(Long JOB_SEEKERId) {
// JOB_SEEKER JOB_SEEKER = repository.findById(JOB_SEEKERId)
// .orElseThrow(() -> new USERNotFoundException("JOB_SEEKER not found with ID: "
// + JOB_SEEKERId));
// return JOB_SEEKER.getName() != null ? JOB_SEEKER.getName() : "JOB_SEEKER";
// }

// @Override
// public JOB_SEEKERDetialsDto getJOB_SEEKERProfile(Long JOB_SEEKERId) {
// JOB_SEEKER JOB_SEEKER = repository.findById(JOB_SEEKERId)
// .orElseThrow(() -> new JOB_SEEKERNotFoundException("JOB_SEEKER not found with
// ID: " + JOB_SEEKERId));
// return JOB_SEEKERMapper.toDto(JOB_SEEKER);
// }

// private JOB_SEEKER getAuthenticatedJOB_SEEKER() {
// Authentication authentication =
// SecurityContextHolder.getContext().getAuthentication();
// if (authentication == null) {
// throw new AccessDeniedException("Not authenticated.");
// }

// String currentJOB_SEEKEREmail = authentication.getName();
// return repository.findByEmail(currentJOB_SEEKEREmail)
// .orElseThrow(() -> new USERNotFoundException("Authenticated JOB_SEEKER not
// found in database"));
// }
// }
