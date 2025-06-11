package com.pravin.job_portal_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.dto.ApplicationProfileDto;
import com.pravin.job_portal_backend.dto.ApplyJobDto;
import com.pravin.job_portal_backend.dto.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.entity.ApplyJob;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.mapper.ApplyJobMapper;
import com.pravin.job_portal_backend.repository.JobApply;
import com.pravin.job_portal_backend.repository.JobsRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.JobApplyService;

@Service
public class JobApplyServiceImpl implements JobApplyService {

    @Autowired
    private JobApply jobApplicationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobsRepository jobRepository;

    @Override
    public String applyForJob(Long userId, Long jobId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (jobApplicationRepository.existsByUserAndJob(user, job)) {
            return "Already applied to this job.";
        }

        ApplyJob application = ApplyJob.builder()
                .user(user)
                .job(job)
                .appliedAt(LocalDateTime.now())
                .build();

        jobApplicationRepository.save(application);

        return "Job application submitted successfully.";
    }

    @Override
    public void deleteApplicationById(Long applicationId) {
        ApplyJob application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        jobApplicationRepository.delete(application);
    }

    @Override
    public void updateApplicationById(Long applicationId, Map<String, Object> updates) {
        ApplyJob application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        // Only allow updating specific fields (add more as needed)
        if (updates.containsKey("status")) {
            // application.setStatus((String) updates.get("status"));
            // Uncomment above and implement if status field exists in ApplyJob entity
        }
        if (updates.containsKey("recruiterRemarks")) {
            // application.setRecruiterRemarks((String) updates.get("recruiterRemarks"));
            // Uncomment above and implement if recruiterRemarks field exists in ApplyJob entity
        }
        // Add more fields as needed
        jobApplicationRepository.save(application);
    }

    @Override
    public CompletableFuture<List<ApplicationProfileDto>> getAllApplicationsWithProfiles() {
        List<ApplyJob> applications = jobApplicationRepository.findAll();
        List<ApplicationProfileDto> result = applications.stream().map(app -> {
            Job job = app.getJob();
            User user = app.getUser();
            return ApplicationProfileDto.builder()
                    .applicationId(app.getId())
                    .jobTitle(job.getTitle())
                    .company(null) // Set company if available
                    .applicantName(user.getName())
                    .applicantEmail(user.getEmail())
                    .applicantProfile(user.getBio())
                    .build();
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public List<ApplyJobResponseDTO> getAppliedJobByUserDTO(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // System.out.println("[DEBUG] Fetching applied jobs for user: " + user.getEmail() + " (ID: " + userId + ")");
        List<ApplyJob> applications = jobApplicationRepository.findByUser(user);
        // System.out.println("[DEBUG] Number of applications found: " + applications.size());
        List<ApplyJobResponseDTO> dtos = applications.stream()
                .map(app -> {
                    Job job = app.getJob();
                    return new ApplyJobResponseDTO(
                        app.getId(), // applicationId
                        job.getTitle(),
                        job.getCompany(),
                        user.getEmail(),
                        app.getAppliedAt(),
                        null, // status (if available)
                        null, // recruiterRemarks (if available)
                        job.getPostedDate(), // LocalDate
                        job.getLocation(),   // location
                        job.getSalary()      // salary
                    );
                })
                .collect(Collectors.toList());
        // System.out.println("[DEBUG] DTOs to return: " + dtos);
        return dtos;
    }

    @Override
    public String cancelApplication(Long userId, Long jobId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        ApplyJob application = jobApplicationRepository.findByUser(user).stream()
                .filter(app -> app.getJob().equals(job))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Application not found"));

        jobApplicationRepository.delete(application);
        return "Application cancelled successfully.";
    }

    @Override
    public List<ApplyJobDto> getAllApplications() {
        return jobApplicationRepository.findAll().stream()
                .map(ApplyJobMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplyJobDto> getApplicationsForJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return jobApplicationRepository.findAll().stream()
                .filter(app -> app.getJob().getId().equals(jobId))
                .map(ApplyJobMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplyJobDto> getApplicationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<ApplyJob> apps = jobApplicationRepository.findByUser(user);
        // System.out.println("[DEBUG] Applications found for user " + userId + ": " + apps.size());
        return apps.stream()
                .map(ApplyJobMapper::toDto)
                .collect(Collectors.toList());
    }
}
