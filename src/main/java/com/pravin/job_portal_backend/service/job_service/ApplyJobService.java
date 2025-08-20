package com.pravin.job_portal_backend.service.job_service;



import java.util.List;

import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobResponseDTO;

public interface ApplyJobService<ApplyJobRequestDTO> {

    ApplyJobResponseDTO applyForJob(ApplyJobRequestDTO requestDTO);

    void withdrawApplication(Long applicationId);

    List<ApplyJobResponseDTO> getApplicationsByUser(Long userId);

    List<ApplyJobResponseDTO> getApplicationsByJob(Long jobId);

    ApplyJobResponseDTO getApplicationById(Long applicationId);

    ApplyJobResponseDTO applyForJob(
            com.pravin.job_portal_backend.dto.job_application_dtos.ApplyJobRequestDTO requestDTO);
}
