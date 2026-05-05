package com.pravin.job_portal_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.InterviewDto;
import com.pravin.job_portal_backend.dto.ScheduleInterviewRequest;
import com.pravin.job_portal_backend.entity.ApplyJob;
import com.pravin.job_portal_backend.entity.Interview;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.mapper.InterviewMapper;
import com.pravin.job_portal_backend.repository.InterviewRepository;
import com.pravin.job_portal_backend.repository.JobApply;
import com.pravin.job_portal_backend.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

  private final InterviewRepository interviewRepository;
  private final JobApply jobApplyRepository;
  private final UserRepository userRepository;

  public InterviewController(InterviewRepository interviewRepository, JobApply jobApplyRepository,
      UserRepository userRepository) {
    this.interviewRepository = interviewRepository;
    this.jobApplyRepository = jobApplyRepository;
    this.userRepository = userRepository;
  }

  @GetMapping("/me")
  public ResponseEntity<List<InterviewDto>> myInterviews(Authentication authentication) {
    User user = userFor(authentication.getName());
    List<Interview> interviews = user.getRole().name().startsWith("COMPANY") || "RECRUITER".equals(user.getRole().name())
        ? interviewRepository.findByEmployerOrderByScheduledTimeDesc(user)
        : interviewRepository.findByCandidateOrderByScheduledTimeDesc(user);
    return ResponseEntity.ok(interviews.stream().map(InterviewMapper::toDto).toList());
  }

  @PostMapping
  public ResponseEntity<InterviewDto> schedule(Authentication authentication,
      @Valid @RequestBody ScheduleInterviewRequest request) {
    User employer = userFor(authentication.getName());
    ApplyJob application = jobApplyRepository.findById(request.applicationId())
        .orElseThrow(() -> new IllegalArgumentException("Application not found."));

    Interview interview = Interview.builder()
        .application(application)
        .candidate(application.getUser())
        .employer(employer)
        .scheduledTime(request.scheduledTime())
        .meetingLink(request.meetingLink())
        .notes(request.notes())
        .status("SCHEDULED")
        .build();
    return ResponseEntity.status(HttpStatus.CREATED).body(InterviewMapper.toDto(interviewRepository.save(interview)));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<InterviewDto> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
    Interview interview = interviewRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Interview not found."));
    interview.setStatus(request.getOrDefault("status", interview.getStatus()));
    return ResponseEntity.ok(InterviewMapper.toDto(interviewRepository.save(interview)));
  }

  private User userFor(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found."));
  }

}
