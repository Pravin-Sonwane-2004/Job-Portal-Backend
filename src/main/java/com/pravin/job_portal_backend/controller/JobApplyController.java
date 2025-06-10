package com.pravin.job_portal_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import com.pravin.job_portal_backend.dto.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.JobApplyService;

@RestController
@RequestMapping("/apply/applications")
public class JobApplyController {

  @Autowired
  private JobApplyService jobApplicationService;

  @Autowired
  private UserRepository userRepository;

  // ✅ 1. Apply to a job
  @PostMapping("/apply")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> applyToJob(Authentication authentication, @RequestParam Long jobId) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
    String response = jobApplicationService.applyForJob(user.getId(), jobId);
    return ResponseEntity.ok(response);
  }

  // Delete application by application ID
  @DeleteMapping("/{applicationId}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<?> deleteApplicationById(@PathVariable Long applicationId) {
      jobApplicationService.deleteApplicationById(applicationId);
      return ResponseEntity.ok("Application deleted successfully.");
  }

  // Update application by application ID
  @PutMapping("/{applicationId}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<?> updateApplicationById(@PathVariable Long applicationId, @RequestBody Map<String, Object> updates) {
      jobApplicationService.updateApplicationById(applicationId, updates);
      return ResponseEntity.ok("Application updated successfully.");
  }

 

  // ✅ 2. Get applied jobs as JobDTO list
  @GetMapping("/my-applied-dto")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<List<ApplyJobResponseDTO>> getAppliedJobsDTO(Authentication authentication) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
    List<ApplyJobResponseDTO> appliedJobs = jobApplicationService.getAppliedJobByUserDTO(user.getId());
    // Debug serialization block commented out to avoid LocalDateTime serialization error
    // try {
    //   ObjectMapper mapper = new ObjectMapper();
    //   String json = mapper.writeValueAsString(appliedJobs);
    //   System.out.println("[DEBUG] Controller JSON response: " + json);
    // } catch (Exception e) {
    //   System.out.println("[DEBUG] Controller JSON serialization error: " + e.getMessage());
    // }
    return ResponseEntity.ok(appliedJobs);
  }

  // ✅ 3. Get applied job entities (raw job objects)
  @GetMapping("/my-applied-entities")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getAppliedJobEntities(Authentication authentication) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
    List<ApplyJobDto> appliedJobs = jobApplicationService.getApplicationsByUser(user.getId());
    return ResponseEntity.ok(appliedJobs);
  }

  // ✅ 4. Cancel application
  @DeleteMapping("/cancel")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> cancelApplication(Authentication authentication, @RequestParam Long jobId) {
    String email = authentication.getName();
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
    String result = jobApplicationService.cancelApplication(user.getId(), jobId);
    return ResponseEntity.ok(result);
  }
}
