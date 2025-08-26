package com.pravin.job_portal_backend.service.apply;

import com.pravin.job_portal_backend.dto.job_dtos.JobApplicationAdminDTO;
import com.pravin.job_portal_backend.dto.job_dtos.JobApplicationResponseDTO;
import com.pravin.job_portal_backend.enums.ApplicationStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface JobApplicationService {

    String applyForJob(Long userId, Long jobId);

    void deleteApplicationById(Long applicationId);

    void updateApplicationById(Long applicationId, ApplicationStatus status);

    List<JobApplicationResponseDTO> getAppliedJobByUserDTO(Long userId);

    List<JobApplicationResponseDTO> getApplicationsForJob(Long jobId);

    String cancelApplication(Long userId, Long jobId);

    CompletableFuture<List<JobApplicationAdminDTO>> getAllApplicationsWithProfiles();
}
