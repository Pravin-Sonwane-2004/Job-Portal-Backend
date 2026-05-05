package com.pravin.job_portal_backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return applyForJob(userId, jobId, null);
    }

    @Override
    @Transactional
    public String applyForJob(Long userId, Long jobId, ApplyJobRequestDTO request) {
        User user = getUser(userId);
        Job job = getJob(jobId);

        if (jobApplicationRepository.existsByUserAndJob(user, job)) {
            return "Already applied to this job.";
        }

        jobApplicationRepository.save(createApplication(user, job, request));
        return "Job application submitted successfully.";
    }

    @Override
    public void deleteApplicationById(Long applicationId) {
        jobApplicationRepository.delete(getApplication(applicationId));
    }

    @Override
    @Transactional
    public void deleteUserApplicationById(String userEmail, Long applicationId) {
        ApplyJob application = getApplication(applicationId);
        assertUserOwnsApplication(userEmail, application);
        jobApplicationRepository.delete(application);
    }

    @Override
    @Transactional
    public void updateApplicationById(Long applicationId, Map<String, Object> updates) {
        ApplyJob application = getApplication(applicationId);
        applyApplicationUpdates(application, updates);
        jobApplicationRepository.save(application);
    }

    @Override
    @Transactional
    public void updateUserApplicationById(String userEmail, Long applicationId, Map<String, Object> updates) {
        ApplyJob application = getApplication(applicationId);
        assertUserOwnsApplication(userEmail, application);
        applyUserEditableApplicationUpdates(application, updates);
        jobApplicationRepository.save(application);
    }

    @Override
    @Transactional
    public void updateRecruiterApplicationById(String recruiterEmail, Long applicationId, Map<String, Object> updates) {
        ApplyJob application = getApplication(applicationId);
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
        }).toList();
        return CompletableFuture.completedFuture(result);
    }

    @Override
    public String cancelApplication(Long userId, Long jobId) {
        User user = getUser(userId);
        Job job = getJob(jobId);

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
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplyJobDto> getApplicationsForRecruiter(String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        return jobApplicationRepository.findByJob_PostedBy(recruiter).stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    @Override
    public List<ApplyJobDto> getApplicationsForJob(Long jobId) {
        Job job = getJob(jobId);

        return jobApplicationRepository.findByJob(job).stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplyJobDto> getApplicationsForRecruiterJob(String recruiterEmail, Long jobId) {
        Job job = getJob(jobId);
        if (job.getPostedBy() == null || !recruiterEmail.equalsIgnoreCase(job.getPostedBy().getEmail())) {
            throw new AccessDeniedException("You can only view applications for your own jobs.");
        }
        return jobApplicationRepository.findByJob(job).stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    @Override
    public List<ApplyJobDto> getApplicationsByUser(Long userId) {
        return jobApplicationRepository.findByUser(getUser(userId)).stream()
                .map(ApplyJobMapper::toDto)
                .toList();
    }

    private void assertRecruiterOwnsApplication(String recruiterEmail, ApplyJob application) {
        Job job = application.getJob();
        if (job == null || job.getPostedBy() == null || !recruiterEmail.equalsIgnoreCase(job.getPostedBy().getEmail())) {
            throw new AccessDeniedException("You can only manage applications for your own jobs.");
        }
    }

    private void assertUserOwnsApplication(String userEmail, ApplyJob application) {
        User user = application.getUser();
        if (user == null || !userEmail.equalsIgnoreCase(user.getEmail())) {
            throw new AccessDeniedException("You can only manage your own applications.");
        }
    }

    @Override
    public List<ApplyJobResponseDTO> getAppliedJobByUserDTO(Long userId) {
        User user = getUser(userId);
        return jobApplicationRepository.findByUser(user).stream()
                .map(app -> toAppliedJobResponse(app, user))
                .toList();
    }

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

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Job getJob(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    private ApplyJob getApplication(Long applicationId) {
        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }
}
