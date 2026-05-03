package com.pravin.job_portal_backend.service.impl;

import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.dto.ProfileInsightsDto;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.repository.JobApply;
import com.pravin.job_portal_backend.repository.JobsRepository;
import com.pravin.job_portal_backend.repository.ResumeRepository;
import com.pravin.job_portal_backend.repository.SavedJobRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.ProfileInsightsService;

@Service
public class ProfileInsightsServiceImpl implements ProfileInsightsService {

  private final UserRepository userRepository;
  private final JobsRepository jobsRepository;
  private final JobApply jobApplyRepository;
  private final SavedJobRepository savedJobRepository;
  private final ResumeRepository resumeRepository;

  public ProfileInsightsServiceImpl(
      UserRepository userRepository,
      JobsRepository jobsRepository,
      JobApply jobApplyRepository,
      SavedJobRepository savedJobRepository,
      ResumeRepository resumeRepository) {
    this.userRepository = userRepository;
    this.jobsRepository = jobsRepository;
    this.jobApplyRepository = jobApplyRepository;
    this.savedJobRepository = savedJobRepository;
    this.resumeRepository = resumeRepository;
  }

  @Override
  public ProfileInsightsDto getInsightsFor(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("Authenticated user was not found."));

    Role role = user.getRole();
    if (Role.ADMIN.equals(role)) {
      return adminInsights(user);
    }
    if (Role.RECRUITER.equals(role)) {
      return recruiterInsights(user);
    }
    if (Role.COMPANY_ADMIN.equals(role) || Role.COMPANY_EMPLOYEE.equals(role)) {
      return companyInsights(user);
    }
    return candidateInsights(user);
  }

  private ProfileInsightsDto candidateInsights(User user) {
    long applications = jobApplyRepository.countByUser(user);
    long savedJobs = savedJobRepository.countByUser(user);
    long resumes = resumeRepository.countByUser(user);
    int completion = profileCompletion(user);
    String nextAction = completion < 70
        ? "Complete your profile to improve recruiter visibility."
        : applications == 0 ? "Apply to jobs that match your profile." : "Track your applications and save matching jobs.";

    return new ProfileInsightsDto(Role.USER, 0, 0, applications, savedJobs, resumes, completion, nextAction);
  }

  private ProfileInsightsDto recruiterInsights(User user) {
    long jobs = jobsRepository.countByPostedBy(user);
    long applications = jobApplyRepository.countByJob_PostedBy(user);
    String nextAction = jobs == 0 ? "Post your first job to start receiving applications."
        : applications == 0 ? "Review your job details and share openings with candidates."
            : "Review new applicants and update their status.";

    return new ProfileInsightsDto(Role.RECRUITER, 0, jobs, applications, 0, 0, profileCompletion(user), nextAction);
  }

  private ProfileInsightsDto companyInsights(User user) {
    long jobs = user.getCompany() == null ? 0 : jobsRepository.countByCompanyIgnoreCase(user.getCompany().getName());
    long applications = user.getCompany() == null ? 0 : jobApplyRepository.countByJob_CompanyIgnoreCase(user.getCompany().getName());
    long employees = user.getCompany() == null ? 0 : userRepository.countByCompany(user.getCompany());
    String nextAction = jobs == 0 ? "Post a company job to start hiring."
        : applications == 0 ? "Invite candidates to apply to your company jobs."
            : "Review applications and schedule interviews.";
    return new ProfileInsightsDto(user.getRole(), employees, jobs, applications, 0, 0, profileCompletion(user), nextAction);
  }

  private ProfileInsightsDto adminInsights(User user) {
    long users = userRepository.count();
    long jobs = jobsRepository.count();
    long applications = jobApplyRepository.count();
    String nextAction = "Monitor platform users, jobs, and application activity.";

    return new ProfileInsightsDto(Role.ADMIN, users, jobs, applications, 0, 0, profileCompletion(user), nextAction);
  }

  private int profileCompletion(User user) {
    long filled = Stream.of(
        user.getName(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getLocation(),
        user.getDesignation(),
        user.getBio(),
        user.getLinkedinUrl(),
        user.getGithubURL(),
        user.getJobRole())
        .filter(Objects::nonNull)
        .filter(value -> !value.isBlank())
        .count();

    if (user.getSkills() != null && !user.getSkills().isEmpty()) {
      filled++;
    }

    return (int) Math.round((filled / 10.0) * 100);
  }
}
