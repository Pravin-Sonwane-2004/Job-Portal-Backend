package com.pravin.job_portal_backend.service.job_service;

import com.pravin.job_portal_backend.dto.job_dtos.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobService {

    // === Core CRUD ===
    JobResponseDTO createJob(JobRequestDTO dto);

    JobResponseDTO updateJob(Long jobId, JobRequestDTO dto);

    void deleteJob(Long jobId); // Hard delete

    JobResponseDTO getJobById(Long jobId);

    // List<JobSummaryDTO> getAllJobs();

    // // === Pagination & Filtering ===
    // Page<JobSummaryDTO> getAllJobsPaginated(
    //         int page,
    //         int size,
    //         String sortBy,
    //         String sortDir,
    //         String jobTitle,
    //         String jobLocation,
    //         Double minSalary,
    //         Double maxSalary);

    // === Queries ===
    List<JobSummaryDTO> getJobsByCompany(Long companyId);

    // List<JobSummaryDTO> getJobsByStatus(String status); // e.g. OPEN, CLOSED, DELETED

    // List<JobSummaryDTO> searchJobs(String keyword, String location); // keyword + location

    // === Admin View ===
    List<JobAdminDTO> getAllJobsForAdmin();

    // === Job State Management ===
    void markJobAsDeleted(Long jobId); // Soft delete

    void restoreJob(Long jobId); // Restore soft-deleted job

    void closeJob(Long jobId); // Mark job as closed
}
