package com.pravin.job_portal_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pravin.job_portal_backend.config.AsyncConfig;
import com.pravin.job_portal_backend.dto.ApplicationProfileDtoAdmin;
import com.pravin.job_portal_backend.dto.ApplyJobDto;
import com.pravin.job_portal_backend.dto.ApplyJobRequestDTO;
import com.pravin.job_portal_backend.dto.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.entity.ApplyJob;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.mapper.ApplyJobMapper;
import com.pravin.job_portal_backend.repository.JobApply;
import com.pravin.job_portal_backend.repository.JobsRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import com.pravin.job_portal_backend.service.interfaces.JobApplyService;

/**
 * Business logic for job applications.
 *
 * Main responsibilities:
 * 1. Load and validate users, jobs, and applications.
 * 2. Prevent duplicate applications.
 * 3. Enforce ownership rules for users and recruiters.
 * 4. Convert saved entities into DTOs for the API layer.
 */
@Service
public class JobApplyServiceImpl implements JobApplyService {

    private final JobApply jobApplicationRepository;
    private final UserRepository userRepository;
    private final JobsRepository jobRepository;

    public JobApplyServiceImpl(JobApply jobApplicationRepository, UserRepository userRepository,
            JobsRepository jobRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public String applyForJob(Long userId, Long jobId) {
        // Backward-compatible helper for callers that do not send extra details.
        return applyForJob(userId, jobId, null);
    }

    @Override
    @Transactional
    public String applyForJob(Long userId, Long jobId, ApplyJobRequestDTO request) {
        // Load real database records instead of trusting ids from the request body.
        User user = getUser(userId);
        Job job = getJob(jobId);

        // Business rule: one user can apply to the same job only once.
        if (jobApplicationRepository.existsByUserAndJob(user, job)) {
            return "Already applied to this job.";
        }

        jobApplicationRepository.save(createApplication(user, job, request));
        return "Job application submitted successfully.";
    }

    @Override
    public void deleteApplicationById(Long applicationId) {
        // Admin path: no ownership check because admin is allowed to remove any application.
        jobApplicationRepository.delete(getApplication(applicationId));
    }

    @Override
    @Transactional
    public void deleteUserApplicationById(String userEmail, Long applicationId) {
        ApplyJob application = getApplication(applicationId);
        // User path: ownership is checked before deleting.
        assertUserOwnsApplication(userEmail, application);
        jobApplicationRepository.delete(application);
    }

    @Override
    @Transactional
    public void updateApplicationById(Long applicationId, Map<String, Object> updates) {
        // Admin update path: status and recruiter remarks can be changed directly.
        ApplyJob application = getApplication(applicationId);
        applyApplicationUpdates(application, updates);
        jobApplicationRepository.save(application);
    }

    @Override
    @Transactional
    public void updateUserApplicationById(String userEmail, Long applicationId, Map<String, Object> updates) {
        ApplyJob application = getApplication(applicationId);
        // Users can edit their own application details, not recruiter/admin fields.
        assertUserOwnsApplication(userEmail, application);
        applyUserEditableApplicationUpdates(application, updates);
        jobApplicationRepository.save(application);
    }

    @Override
    @Transactional
    public void updateRecruiterApplicationById(String recruiterEmail, Long applicationId, Map<String, Object> updates) {
        ApplyJob application = getApplication(applicationId);
        // Recruiters can update applications only for jobs they posted.
        assertRecruiterOwnsApplication(recruiterEmail, application);
        applyApplicationUpdates(application, updates);
        jobApplicationRepository.save(application);
    }

    /**
     * Admin/recruiter editable fields.
     */
    private void applyApplicationUpdates(ApplyJob application, Map<String, Object> updates) {
        if (updates.containsKey("status")) {
            application.setStatus(String.valueOf(updates.get("status")));
        }
        if (updates.containsKey("recruiterRemarks")) {
            Object value = updates.get("recruiterRemarks");
            application.setRecruiterRemarks(value != null ? String.valueOf(value) : null);
        }
    }

    /**
     * Candidate editable fields. Status and recruiter remarks are not handled here
     * because candidates should not control the hiring decision.
     */
    private void applyUserEditableApplicationUpdates(ApplyJob application, Map<String, Object> updates) {
        if (updates.containsKey("resumeLink")) application.setResumeLink(stringValue(updates.get("resumeLink")));
        if (updates.containsKey("coverLetter")) application.setCoverLetter(stringValue(updates.get("coverLetter")));
        if (updates.containsKey("phoneNumber")) application.setPhoneNumber(stringValue(updates.get("phoneNumber")));
        if (updates.containsKey("linkedinUrl")) application.setLinkedinUrl(stringValue(updates.get("linkedinUrl")));
        if (updates.containsKey("portfolioUrl")) application.setPortfolioUrl(stringValue(updates.get("portfolioUrl")));
        if (updates.containsKey("expectedSalary")) application.setExpectedSalary(stringValue(updates.get("expectedSalary")));
        if (updates.containsKey("noticePeriod")) application.setNoticePeriod(stringValue(updates.get("noticePeriod")));
    }

    private String stringValue(Object value) {
        return value != null ? String.valueOf(value) : null;
    }

    @Override
    @Async(AsyncConfig.APPLICATION_TASK_EXECUTOR)
    @Transactional(readOnly = true)
    public CompletableFuture<List<ApplicationProfileDtoAdmin>> getAllApplicationsWithProfiles() {
        // This method runs on AsyncConfig.APPLICATION_TASK_EXECUTOR, not the HTTP request thread.
        // The transaction is opened inside that async thread so JPA can safely read related entities.
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
        }).toList();
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public String cancelApplication(Long userId, Long jobId) {
        User user = getUser(userId);
        Job job = getJob(jobId);

        // Cancel uses jobId because users usually cancel from a job card/details page.
        ApplyJob application = jobApplicationRepository.findByUser(user).stream()
                .filter(app -> app.getJob().equals(job))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Application not found"));

        jobApplicationRepository.delete(application);
        return "Application cancelled successfully.";
    }

    @Override
    public List<ApplyJobDto> getAllApplications() {
        // Admin list: returns every application in the system.
        return jobApplicationRepository.findAll().stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplyJobDto> getApplicationsForRecruiter(String recruiterEmail) {
        // Recruiter dashboard list: only applications for jobs posted by this recruiter.
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return jobApplicationRepository.findByJob_PostedBy(recruiter).stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    @Override
    public List<ApplyJobDto> getApplicationsForJob(Long jobId) {
        // Admin/general lookup by job. Recruiter-specific access is checked in another method.
        Job job = getJob(jobId);

        return jobApplicationRepository.findByJob(job).stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplyJobDto> getApplicationsForRecruiterJob(String recruiterEmail, Long jobId) {
        Job job = getJob(jobId);
        // Important security rule: a recruiter cannot view another recruiter's applicants.
        if (job.getPostedBy() == null || !recruiterEmail.equalsIgnoreCase(job.getPostedBy().getEmail())) {
            throw new AccessDeniedException("You can only view applications for your own jobs.");
        }
        return jobApplicationRepository.findByJob(job).stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    @Override
    public List<ApplyJobDto> getApplicationsByUser(Long userId) {
        // User profile/dashboard list: all applications submitted by one candidate.
        return jobApplicationRepository.findByUser(getUser(userId)).stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    /** Verifies recruiter ownership before viewing or changing an application. */
    private void assertRecruiterOwnsApplication(String recruiterEmail, ApplyJob application) {
        Job job = application.getJob();
        if (job == null || job.getPostedBy() == null || !recruiterEmail.equalsIgnoreCase(job.getPostedBy().getEmail())) {
            throw new AccessDeniedException("You can only manage applications for your own jobs.");
        }
    }

    /** Verifies candidate ownership before editing or deleting an application. */
    private void assertUserOwnsApplication(String userEmail, ApplyJob application) {
        User user = application.getUser();
        if (user == null || !userEmail.equalsIgnoreCase(user.getEmail())) {
            throw new AccessDeniedException("You can only manage your own applications.");
        }
    }

    @Override
    public List<ApplyJobResponseDTO> getAppliedJobByUserDTO(Long userId) {
        // Response DTO is shaped for the "My applied jobs" screen.
        User user = getUser(userId);
        return jobApplicationRepository.findByUser(user).stream()
                .map(app -> toAppliedJobResponse(app, user))
                .toList();
    }

    /** Creates the initial application entity with default status/source values. */
    private ApplyJob createApplication(User user, Job job, ApplyJobRequestDTO request) {
        ApplyJob application = ApplyJob.builder()
                .user(user)
                .job(job)
                .appliedAt(LocalDateTime.now())
                .status("APPLIED")
                .source("Web")
                .build();
        applyRequestDetails(application, request);
        return application;
    }

    /** Copies optional candidate-supplied fields onto the new application. */
    private void applyRequestDetails(ApplyJob application, ApplyJobRequestDTO request) {
        if (request == null) {
            return;
        }
        application.setResumeLink(request.getResumeLink());
        application.setCoverLetter(request.getCoverLetter());
        application.setPhoneNumber(request.getPhoneNumber());
        application.setLinkedinUrl(request.getLinkedinUrl());
        application.setPortfolioUrl(request.getPortfolioUrl());
        application.setExpectedSalary(request.getExpectedSalary());
        application.setNoticePeriod(request.getNoticePeriod());
        application.setAppliedFromIp(request.getAppliedFromIp());
        application.setSource(request.getSource() != null ? request.getSource() : "Web");
        application.setUserAgent(request.getUserAgent());
    }

    /** Builds the compact response used by the user's applied-jobs page. */
    private ApplyJobResponseDTO toAppliedJobResponse(ApplyJob application, User user) {
        Job job = application.getJob();
        return new ApplyJobResponseDTO(
                application.getId(),
                job != null ? job.getTitle() : null,
                job != null ? job.getCompany() : null,
                job != null ? job.getLocation() : null,
                job != null ? job.getSalary() : null,
                job != null ? job.getPostedDate() : null,
                user.getEmail(),
                application.getAppliedAt(),
                application.getUpdatedAt(),
                application.getStatus(),
                application.getRecruiterRemarks(),
                application.getResumeLink(),
                application.getCoverLetter(),
                application.getPhoneNumber(),
                application.getLinkedinUrl(),
                application.getPortfolioUrl(),
                application.getExpectedSalary(),
                application.getNoticePeriod(),
                application.getAppliedFromIp(),
                application.getSource(),
                application.getUserAgent());
    }

    /** Shared helper for loading users with a clear error message. */
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /** Shared helper for loading jobs with a clear error message. */
    private Job getJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    /** Shared helper for loading applications with a clear error message. */
    private ApplyJob getApplication(Long applicationId) {
        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }
}
