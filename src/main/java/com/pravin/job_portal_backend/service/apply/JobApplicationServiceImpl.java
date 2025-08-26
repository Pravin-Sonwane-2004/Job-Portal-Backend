package com.pravin.job_portal_backend.service.apply;

import com.pravin.job_portal_backend.dto.job_dtos.JobApplicationAdminDTO;
import com.pravin.job_portal_backend.dto.job_dtos.JobApplicationResponseDTO;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.JobApplication;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.ApplicationStatus;
import com.pravin.job_portal_backend.mapper.job_application_mapper.JobApplicationMapper;
import com.pravin.job_portal_backend.repository.JobApplicationRepository;
import com.pravin.job_portal_backend.repository.JobRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public String applyForJob(Long userId, Long jobId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        boolean alreadyApplied = jobApplicationRepository.existsByUserAndJob(user, job);
        if (alreadyApplied) {
            throw new RuntimeException("You have already applied to this job");
        }

        JobApplication application = JobApplication.builder()
                .user(user)
                .job(job)
                .status(ApplicationStatus.APPLIED)
                .build();

        jobApplicationRepository.save(application);
        return "Application submitted successfully!";
    }

    @Override
    public void deleteApplicationById(Long applicationId) {
        jobApplicationRepository.deleteById(applicationId);
    }

    @Override
    public void updateApplicationById(Long applicationId, ApplicationStatus status) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        jobApplicationRepository.save(application);
    }

    @Override
    public List<JobApplicationResponseDTO> getAppliedJobByUserDTO(Long userId) {
        return jobApplicationRepository.findByUserId(userId).stream()
                .map(JobApplicationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplicationResponseDTO> getApplicationsForJob(Long jobId) {
        return jobApplicationRepository.findByJobId(jobId).stream()
                .map(JobApplicationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public String cancelApplication(Long userId, Long jobId) {
        JobApplication application = jobApplicationRepository.findByUserIdAndJobId(userId, jobId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        jobApplicationRepository.delete(application);
        return "Application cancelled successfully!";
    }

    @Override
    public CompletableFuture<List<JobApplicationAdminDTO>> getAllApplicationsWithProfiles() {
        return CompletableFuture.supplyAsync(() -> jobApplicationRepository.findAll().stream()
                .map(JobApplicationMapper::toAdmin)
                .collect(Collectors.toList()));
    }
}
