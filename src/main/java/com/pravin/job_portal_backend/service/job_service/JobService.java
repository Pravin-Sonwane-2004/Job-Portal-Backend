package com.pravin.job_portal_backend.service.job_service;

import com.pravin.job_portal_backend.dto.job_dtos.*;

import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {

    // === Create ===
    JobResponseDTO createJob(JobRequestDTO jobDto);

    // === Update ===
    JobResponseDTO updateJob(Long jobId, JobRequestDTO jobDto);

    // === Delete (Hard Delete if admin, or soft delete flag) ===
    void deleteJob(Long jobId);

    // === Get by ID ===
    JobResponseDTO getJobById(Long jobId);

    // === List all (for JOB_SEEKER display, lightweight summaries) ===
    List<JobSummaryDTO> getAllJobs();

    // === Search by keyword & location ===
    List<JobSummaryDTO> searchJobs(String keyword, String location);

    // === Paginated & Filtered Jobs ===
    Page<JobSummaryDTO> getAllJobsPaginated(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String jobTitle,
            String jobLocation,
            Double minSalary,
            Double maxSalary);

    // === For Admin Panel (with extra details) ===
    List<JobAdminDTO> getAllJobsForAdmin();

    // === Job Lifecycle Management ===
    void closeJob(Long jobId); // Mark job as closed

    void markJobAsDeleted(Long jobId); // Soft delete

    void restoreJob(Long jobId); // Restore a deleted job
}
