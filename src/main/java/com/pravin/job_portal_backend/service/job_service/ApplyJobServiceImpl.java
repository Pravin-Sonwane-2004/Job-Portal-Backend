package com.pravin.job_portal_backend.service.job_service;


import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobDto;
import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.dto.job_application_dtos.ApplyJobRequestDTO;
import com.pravin.job_portal_backend.entity.ApplyJob;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplyJobServiceImpl implements ApplyJobService {

    private final ApplyJobRepository applyJobRepository;
    private final UserRepository userRepository;
    private final JobsRepository jobRepository;

    @Override
    public ApplyJobResponseDTO applyForJob(ApplyJobRequestDTO requestDTO) {
        // Validate User
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + requestDTO.getUserId()));

        // Validate Job
        Job job = jobRepository.findById(requestDTO.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + requestDTO.getJobId()));

        // Check if already applied
        if (applyJobRepository.existsByUserIdAndJobId(user.getId(), job.getId())) {
            throw new RuntimeException("User has already applied for this job");
        }

        // Save application
        ApplyJob applyJob = ApplyJob.builder()
                .user(user)
                .job(job)
                .coverLetter(requestDTO.getCoverLetter())
                .status("APPLIED") // default status
                .build();

        ApplyJob saved = applyJobRepository.save(applyJob);

        return toResponseDTO(saved);
    }

    @Override
    public void withdrawApplication(Long applicationId) {
        ApplyJob applyJob = applyJobRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));
        applyJobRepository.delete(applyJob);
    }

    @Override
    public List<ApplyJobResponseDTO> getApplicationsByUser(Long userId) {
        return applyJobRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplyJobResponseDTO> getApplicationsByJob(Long jobId) {
        return applyJobRepository.findByJobId(jobId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ApplyJobResponseDTO getApplicationById(Long applicationId) {
        ApplyJob applyJob = applyJobRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with ID: " + applicationId));
        return toResponseDTO(applyJob);
    }

    // === Mapping ===
    private ApplyJobResponseDTO toResponseDTO(ApplyJob applyJob) {
        return ApplyJobResponseDTO.builder()
                .id(applyJob.getId())
                .userId(applyJob.getUser().getId())
                .jobId(applyJob.getJob().getId())
                .coverLetter(applyJob.getCoverLetter())
                .status(applyJob.getStatus())
                .appliedAt(applyJob.getAppliedAt())
                .build();
    }

    @Override
    public ApplyJobResponseDTO applyForJob(Object requestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyForJob'");
    }
}
