package com.pravin.job_portal_backend.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.ApplyJobDto;
import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.dto.UpdateUserProfile;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.dto.UserLoginDTO;
import com.pravin.job_portal_backend.service.interfaces.AdminUserService;
import com.pravin.job_portal_backend.service.interfaces.JobApplyService;
import com.pravin.job_portal_backend.service.interfaces.JobService;
import com.pravin.job_portal_backend.service.interfaces.UserProfileService;
import com.pravin.job_portal_backend.service.interfaces.UserRegistrationService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

  private static final Logger log = LoggerFactory.getLogger(AdminController.class);

  @Autowired
  private JobApplyService jobApplicationService;

  @Autowired
  private AdminUserService adminUserService;

  @Autowired
  private UserRegistrationService userRegistrationService;

  @Autowired
  private JobService jobService;

  // @Autowired
  // private PasswordEncoder passwordEncoder;

  @Autowired
  private UserProfileService userProfileService;

  @GetMapping("/users")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> users = adminUserService.getAll();
    if (users == null || users.isEmpty())
      return ResponseEntity.noContent().build();
    return ResponseEntity.ok(users);
  }
   // Admin: Get all applications with applicant profiles
  @GetMapping("/admin/all-appliers")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> getAllApplicationsWithProfiles() {
    try {
      // Await the CompletableFuture and return the result
      List<?> allApplications = jobApplicationService.getAllApplicationsWithProfiles().join();
      return ResponseEntity.ok(allApplications);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch applications: " + e.getMessage());
    }
  }

  @DeleteMapping("/user/{username}")
  public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
    try {
      adminUserService.deleteByUsername(username);
      return ResponseEntity.ok("User deleted successfully.");
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
    }
  }

  @PutMapping("/user/{username}")
  public ResponseEntity<String> updateUser(@PathVariable String username,
      @RequestBody UpdateUserProfile updateUserProfileDto) {
    Optional<UserDto> optionalUser = userRegistrationService.findByEmail(username);
    if (optionalUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
    }
    UserDto user = optionalUser.get();
    userProfileService.updateUserProfile(user.getId(), updateUserProfileDto);
    return ResponseEntity.ok("User updated successfully.");
  }

  @PostMapping("/jobs")
  public ResponseEntity<?> createJob(org.springframework.security.core.Authentication authentication, @RequestBody JobDto jobDTO) {
    log.info("Received job creation request: {}", jobDTO);
    if (authentication != null) {
      log.info("Authenticated user: {}", authentication.getName());
      log.info("Authorities: {}", authentication.getAuthorities());
    } else {
      log.warn("No authentication found for this request!");
    }
    if (jobDTO.getLocation() == null || jobDTO.getLocation().trim().isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job location is required.");
    }
    if (jobDTO.getTitle() == null || jobDTO.getTitle().trim().isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job title is required.");
    }
    if (jobDTO.getSalary() == null || jobDTO.getSalary().trim().isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job salary is required.");
    }
    // Optionally, add more validation here
    JobDto created = jobService.createJob(jobDTO);
    log.info("Job created successfully: {}", created);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
  }

  @PutMapping("/jobs/{id}")
  public ResponseEntity<JobDto> updateJob(@PathVariable Long id, @RequestBody JobDto jobDTO) {
    try {
      JobDto updated = jobService.updateJob(id, jobDTO);
      return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @DeleteMapping("/jobs/{id}")
  public ResponseEntity<String> deleteJob(@PathVariable Long id) {
    try {
      jobService.deleteJob(id);
      return ResponseEntity.ok("Job deleted successfully.");
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found.");
    }
  }

  @GetMapping("/applications")
  public ResponseEntity<List<ApplyJobDto>> getAllApplications() {
    List<ApplyJobDto> applications = jobApplicationService.getAllApplications();
    return applications == null || applications.isEmpty() ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(applications);
  }

  @GetMapping("/applications/job/{jobId}")
  public ResponseEntity<List<ApplyJobDto>> getApplicationsForJob(@PathVariable Long jobId) {
    List<ApplyJobDto> applications = jobApplicationService.getApplicationsForJob(jobId);
    return applications == null || applications.isEmpty() ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(applications);
  }

  @GetMapping("/applications/user/{userId}")
  public ResponseEntity<List<ApplyJobDto>> getApplicationsByUser(@PathVariable Long userId) {
    List<ApplyJobDto> applications = jobApplicationService.getApplicationsByUser(userId);
    return applications == null || applications.isEmpty() ? ResponseEntity.noContent().build()
        : ResponseEntity.ok(applications);
  }

  @PostMapping("/signup")
  public ResponseEntity<String> adminSignup(@RequestBody UserLoginDTO userDTO) {
    Optional<UserDto> user = userRegistrationService.saveUser(userDTO);
    if (user.isPresent()) {
      return ResponseEntity.ok("User registered successfully with ID: " + user.get().getId());
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup failed.");
    }
  }
}