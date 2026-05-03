package com.pravin.job_portal_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.CreateJobAlertRequest;
import com.pravin.job_portal_backend.dto.JobAlertDto;
import com.pravin.job_portal_backend.entity.JobAlert;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.JobAlertRepository;
import com.pravin.job_portal_backend.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/job-alerts")
public class JobAlertController {

  private final JobAlertRepository jobAlertRepository;
  private final UserRepository userRepository;

  public JobAlertController(JobAlertRepository jobAlertRepository, UserRepository userRepository) {
    this.jobAlertRepository = jobAlertRepository;
    this.userRepository = userRepository;
  }

  @GetMapping("/me")
  public ResponseEntity<List<JobAlertDto>> myAlerts(Authentication authentication) {
    User user = userFor(authentication);
    return ResponseEntity.ok(jobAlertRepository.findByUserOrderByCreatedAtDesc(user).stream().map(this::toDto).toList());
  }

  @PostMapping
  public ResponseEntity<JobAlertDto> createAlert(Authentication authentication,
      @Valid @RequestBody CreateJobAlertRequest request) {
    User user = userFor(authentication);
    JobAlert alert = JobAlert.builder()
        .user(user)
        .keywords(request.keywords())
        .location(request.location())
        .active(true)
        .build();
    return ResponseEntity.status(HttpStatus.CREATED).body(toDto(jobAlertRepository.save(alert)));
  }

  @DeleteMapping("/{alertId}")
  public ResponseEntity<Void> deleteAlert(@PathVariable Long alertId) {
    jobAlertRepository.deleteById(alertId);
    return ResponseEntity.noContent().build();
  }

  private User userFor(Authentication authentication) {
    return userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
  }

  private JobAlertDto toDto(JobAlert alert) {
    return new JobAlertDto(alert.getId(), alert.getKeywords(), alert.getLocation(), alert.isActive(), alert.getCreatedAt());
  }
}
