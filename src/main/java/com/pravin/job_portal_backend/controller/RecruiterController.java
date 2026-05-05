package com.pravin.job_portal_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.ApplyJobDto;
import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.dto.UserDto;
import com.pravin.job_portal_backend.service.interfaces.JobApplyService;
import com.pravin.job_portal_backend.service.interfaces.JobService;
import com.pravin.job_portal_backend.service.interfaces.UserService;

@RestController
@RequestMapping("/recruiter")
@PreAuthorize("hasRole('RECRUITER')")
public class RecruiterController {

  private final JobService jobService;
  private final JobApplyService jobApplyService;
  private final UserService userService;

  public RecruiterController(JobService jobService, JobApplyService jobApplyService, UserService userService) {
    this.jobService = jobService;
    this.jobApplyService = jobApplyService;
    this.userService = userService;
  }

  @GetMapping("/jobs")
  public ResponseEntity<List<JobDto>> getMyJobs(Authentication authentication) {
    return ResponseEntity.ok(jobService.getJobsByRecruiter(authentication.getName()));
  }

  @PostMapping("/jobs")
  public ResponseEntity<JobDto> createJob(Authentication authentication, @RequestBody JobDto jobDto) {
    JobDto created = jobService.createJobForRecruiter(jobDto, authentication.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/jobs/{id}")
  public ResponseEntity<JobDto> getMyJob(Authentication authentication, @PathVariable Long id) {
    return ResponseEntity.ok(jobService.getRecruiterJobById(id, authentication.getName()));
  }

  @PutMapping("/jobs/{id}")
  public ResponseEntity<JobDto> updateJob(Authentication authentication, @PathVariable Long id,
      @RequestBody JobDto jobDto) {
    return ResponseEntity.ok(jobService.updateRecruiterJob(id, jobDto, authentication.getName()));
  }

  @DeleteMapping("/jobs/{id}")
  public ResponseEntity<?> deleteJob(Authentication authentication, @PathVariable Long id) {
    jobService.deleteRecruiterJob(id, authentication.getName());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/applications")
  public ResponseEntity<List<ApplyJobDto>> getApplications(Authentication authentication) {
    return ResponseEntity.ok(jobApplyService.getApplicationsForRecruiter(authentication.getName()));
  }

  @GetMapping("/jobs/{jobId}/applications")
  public ResponseEntity<List<ApplyJobDto>> getApplicationsForJob(Authentication authentication,
      @PathVariable Long jobId) {
    return ResponseEntity.ok(jobApplyService.getApplicationsForRecruiterJob(authentication.getName(), jobId));
  }

  @PutMapping("/applications/{applicationId}")
  public ResponseEntity<?> updateApplication(Authentication authentication, @PathVariable Long applicationId,
      @RequestBody Map<String, Object> updates) {
    jobApplyService.updateRecruiterApplicationById(authentication.getName(), applicationId, updates);
    return ResponseEntity.ok("Application updated successfully.");
  }

  @GetMapping("/talent")
  public ResponseEntity<List<UserDto>> searchTalent(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) String skill,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) String experienceLevel) {
    return ResponseEntity.ok(userService.searchTalent(q, skill, location, experienceLevel));
  }

  @GetMapping("/talent/{userId}")
  public ResponseEntity<UserDto> getTalentProfile(@PathVariable Long userId) {
    UserDto talent = userService.getTalentProfile(userId);
    return talent != null ? ResponseEntity.ok(talent) : ResponseEntity.notFound().build();
  }
}
