package com.pravin.job_portal_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pravin.job_portal_backend.dto.ApplyJobDto;
import com.pravin.job_portal_backend.dto.ApplyJobRequestDTO;
import com.pravin.job_portal_backend.dto.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.JobApplyService;

/**
 * Job application API for users and admins.
 *
 * Flow:
 * Controller resolves the logged-in user -> delegates business rules to
 * JobApplyService -> returns simple success messages or DTO responses.
 */
@RestController
@RequestMapping("/apply/applications")
public class JobApplyController {

  private final JobApplyService jobApplicationService;
  private final UserRepository userRepository;

  public JobApplyController(JobApplyService jobApplicationService, UserRepository userRepository) {
    this.jobApplicationService = jobApplicationService;
    this.userRepository = userRepository;
  }

  /**
   * User applies for a job. The authenticated user's email comes from JWT/Spring
   * Security, so the client does not choose the userId.
   */
  @PostMapping("/apply")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> applyToJob(Authentication authentication, @RequestParam Long jobId,
      @RequestBody(required = false) ApplyJobRequestDTO request) {
    return ResponseEntity.ok(jobApplicationService.applyForJob(currentUserId(authentication), jobId, request));
  }

  /**
   * Admin can delete any application; normal users can delete only their own.
   */
  @DeleteMapping("/{applicationId}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<?> deleteApplicationById(Authentication authentication, @PathVariable Long applicationId) {
    if (hasRole(authentication, "ROLE_ADMIN")) {
      jobApplicationService.deleteApplicationById(applicationId);
    } else {
      jobApplicationService.deleteUserApplicationById(authentication.getName(), applicationId);
    }
    return ResponseEntity.ok("Application deleted successfully.");
  }

  /**
   * Admin can update status/remarks; users can only update their own editable
   * application details.
   */
  @PutMapping("/{applicationId}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<?> updateApplicationById(Authentication authentication, @PathVariable Long applicationId,
      @RequestBody Map<String, Object> updates) {
    if (hasRole(authentication, "ROLE_ADMIN")) {
      jobApplicationService.updateApplicationById(applicationId, updates);
    } else {
      jobApplicationService.updateUserApplicationById(authentication.getName(), applicationId, updates);
    }
    return ResponseEntity.ok("Application updated successfully.");
  }

  /**
   * Returns a frontend-friendly view of the user's applied jobs.
   */
  @GetMapping("/my-applied-dto")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<ApplyJobResponseDTO>> getAppliedJobsDTO(Authentication authentication) {
    return ResponseEntity.ok(jobApplicationService.getAppliedJobByUserDTO(currentUserId(authentication)));
  }

  /**
   * Returns richer application objects with nested user/job DTOs.
   */
  @GetMapping("/my-applied-entities")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<ApplyJobDto>> getAppliedJobEntities(Authentication authentication) {
    return ResponseEntity.ok(jobApplicationService.getApplicationsByUser(currentUserId(authentication)));
  }

  /**
   * Cancels an application by job id for the currently logged-in user.
   */
  @DeleteMapping("/cancel")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> cancelApplication(Authentication authentication, @RequestParam Long jobId) {
    return ResponseEntity.ok(jobApplicationService.cancelApplication(currentUserId(authentication), jobId));
  }

  /** Converts the authenticated email into the database user id. */
  private Long currentUserId(Authentication authentication) {
    return currentUser(authentication).getId();
  }

  /** Central helper so all endpoints resolve the logged-in user consistently. */
  private User currentUser(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));
  }

  /** Checks Spring Security authorities such as ROLE_USER or ROLE_ADMIN. */
  private boolean hasRole(Authentication authentication, String role) {
    return authentication != null && authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(role::equals);
  }
}
