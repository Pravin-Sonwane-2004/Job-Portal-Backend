package com.pravin.job_portal_backend.service.job_service;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import java.util.List;

public interface JobService {

    // === Core CRUD ===
    JobResponseDTO createJob(JobRequestDTO dto);

    JobResponseDTO updateJob(Long jobId, JobRequestDTO dto);

    void deleteJob(Long jobId); // Hard delete

    JobResponseDTO getJobById(Long jobId);

    List<JobResponseDTO> getAllJobs(); // 🔥 Changed to ResponseDTO

    // === Queries ===
    List<JobSummaryDTO> getJobsByCompany(Long companyId);

    // === Admin View ===
    List<JobAdminDTO> getAllJobsForAdmin();

    // === Job State Management ===
    void markJobAsDeleted(Long jobId); // Soft delete

    void restoreJob(Long jobId); // Restore soft-deleted job

    void closeJob(Long jobId); // Mark job as closed
}
