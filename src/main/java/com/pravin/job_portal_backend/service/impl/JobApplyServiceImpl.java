package com.pravin.job_portal_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.job_portal_backend.dto.ApplicationProfileDtoAdmin;
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
                .status("APPLIED")
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
    @Transactional
    public void updateApplicationById(Long applicationId, Map<String, Object> updates) {
        ApplyJob application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        applyApplicationUpdates(application, updates);
        jobApplicationRepository.save(application);
    }

    @Override
    @Transactional
    public void updateRecruiterApplicationById(String recruiterEmail, Long applicationId, Map<String, Object> updates) {
        ApplyJob application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        assertRecruiterOwnsApplication(recruiterEmail, application);
        applyApplicationUpdates(application, updates);
        jobApplicationRepository.save(application);
    }

    private void applyApplicationUpdates(ApplyJob application, Map<String, Object> updates) {
        if (updates.containsKey("status")) {
            application.setStatus(String.valueOf(updates.get("status")));
        }
        if (updates.containsKey("recruiterRemarks")) {
            Object value = updates.get("recruiterRemarks");
            application.setRecruiterRemarks(value != null ? String.valueOf(value) : null);
        }
    }

    @Override
    public CompletableFuture<List<ApplicationProfileDtoAdmin>> getAllApplicationsWithProfiles() {
        List<ApplyJob> applications = jobApplicationRepository.findAll();
        List<ApplicationProfileDtoAdmin> result = applications.stream().map(app -> {
            Job job = app.getJob();
            User user = app.getUser();
            return ApplicationProfileDtoAdmin.builder()
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

    // @Override
    // public List<ApplyJobResponseDTO> getAppliedJobByUserDTO(Long userId) {
    //     User user = userRepository.findById(userId)
    //             .orElseThrow(() -> new RuntimeException("User not found"));
    //     // System.out.println("[DEBUG] Fetching applied jobs for user: " + user.getEmail() + " (ID: " + userId + ")");
    //     List<ApplyJob> applications = jobApplicationRepository.findByUser(user);
    //     // System.out.println("[DEBUG] Number of applications found: " + applications.size());
        // List<ApplyJobResponseDTO> dtos = applications.stream()
        //         // .map(app -> {
        //         //     Job job = app.getJob();
        //         //     return new ApplyJobResponseDTO(
        //         //         app.getId(), // applicationId
        //         //         job.getTitle(),
        //         //         job.getCompany(),
        //         //         user.getEmail(),
        //         //         app.getAppliedAt(),
        //         //         null, // status (if available)
        //         //         null, // recruiterRemarks (if available)
        //         //         job.getPostedDate(), // LocalDate
        //         //         job.getLocation(),   // location
        //         //         job.getSalary()      // salary
        //         //     );
        //         // })
        //         .collect(Collectors.toList());
        // // System.out.println("[DEBUG] DTOs to return: " + dtos);
        // return dtos;
    // }

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
    @Transactional(readOnly = true)
    public List<ApplyJobDto> getApplicationsForRecruiter(String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return jobApplicationRepository.findByJob_PostedBy(recruiter).stream()
                .map(ApplyJobMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplyJobDto> getApplicationsForJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return jobApplicationRepository.findByJob(job).stream()
                .map(ApplyJobMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplyJobDto> getApplicationsForRecruiterJob(String recruiterEmail, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        if (job.getPostedBy() == null || !recruiterEmail.equalsIgnoreCase(job.getPostedBy().getEmail())) {
            throw new AccessDeniedException("You can only view applications for your own jobs.");
        }
        return jobApplicationRepository.findByJob(job).stream()
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

    private void assertRecruiterOwnsApplication(String recruiterEmail, ApplyJob application) {
        Job job = application.getJob();
        if (job == null || job.getPostedBy() == null || !recruiterEmail.equalsIgnoreCase(job.getPostedBy().getEmail())) {
            throw new AccessDeniedException("You can only manage applications for your own jobs.");
        }
    }

    @Override
    public List<ApplyJobResponseDTO> getAppliedJobByUserDTO(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return jobApplicationRepository.findByUser(user).stream()
                .map(app -> {
                    Job job = app.getJob();
                    return new ApplyJobResponseDTO(
                            app.getId(),
                            job != null ? job.getTitle() : null,
                            job != null ? job.getCompany() : null,
                            job != null ? job.getLocation() : null,
                            job != null ? job.getSalary() : null,
                            job != null ? job.getPostedDate() : null,
                            user.getEmail(),
                            app.getAppliedAt(),
                            app.getUpdatedAt(),
                            app.getStatus(),
                            app.getRecruiterRemarks(),
                            app.getResumeLink(),
                            app.getCoverLetter(),
                            app.getAppliedFromIp(),
                            app.getSource(),
                            app.getUserAgent());
                })
                .collect(Collectors.toList());
    }
}
