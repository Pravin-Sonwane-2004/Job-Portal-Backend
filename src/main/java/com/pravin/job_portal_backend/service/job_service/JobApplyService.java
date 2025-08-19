// package com.pravin.job_portal_backend.service.job_service;

// import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobDto;
// import com.pravin.job_portal_backend.dto.apply_job_dtos.ApplyJobResponseDTO;
// import com.pravin.job_portal_backend.dto.job_dtos.ApplicationProfileDtoAdmin;

// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.CompletableFuture;

// public interface JobApplyService {

//     /**
//      * Apply for a job by a JOB_SEEKER
//      *
//      * @param JOB_SEEKERId ID of the JOB_SEEKER applying
//      * @param jobId  ID of the job
//      * @return Success/failure message
//      */
//     String applyForJob(Long JOB_SEEKERId, Long jobId);

//     /**
//      * Get all job applications along with applicant profiles (for admin)
//      *
//      * @return List of ApplicationProfileDtoAdmin wrapped in CompletableFuture
//      */
//     CompletableFuture<List<ApplicationProfileDtoAdmin>> getAllApplicationsWithProfiles();

//     /**
//      * Get applied jobs for a specific JOB_SEEKER (JOB_SEEKER-facing response)
//      *
//      * @param JOB_SEEKERId ID of the JOB_SEEKER
//      * @return List of ApplyJobResponseDTO
//      */
//     List<ApplyJobResponseDTO> getAppliedJobByJOB_SEEKERDTO(Long JOB_SEEKERId);

//     /**
//      * Cancel a specific job application by a JOB_SEEKER
//      *
//      * @param JOB_SEEKERId ID of the JOB_SEEKER
//      * @param jobId  ID of the job
//      * @return Success/failure message
//      */
//     String cancelApplication(Long JOB_SEEKERId, Long jobId);

//     /**
//      * Get all applications (lightweight DTO)
//      *
//      * @return List of ApplyJobDto
//      */
//     List<ApplyJobDto> getAllApplications();

//     /**
//      * Get all applications for a specific job
//      *
//      * @param jobId ID of the job
//      * @return List of ApplyJobDto
//      */
//     List<ApplyJobDto> getApplicationsForJob(Long jobId);

//     /**
//      * Get all applications by a specific JOB_SEEKER
//      *
//      * @param JOB_SEEKERId ID of the JOB_SEEKER
//      * @return List of ApplyJobDto
//      */
//     List<ApplyJobDto> getApplicationsByJOB_SEEKER(Long JOB_SEEKERId);

//     /**
//      * Delete an application by its ID
//      *
//      * @param applicationId ID of the application
//      */
//     void deleteApplicationById(Long applicationId);

//     /**
//      * Update an application by its ID with dynamic fields (status, resume link,
//      * etc.)
//      *
//      * @param applicationId ID of the application
//      * @param updates       Map of fields to update
//      */
//     void updateApplicationById(Long applicationId, Map<String, Object> updates);

//     List<ApplyJobResponseDTO> getAppliedJobByUserDTO(Long userId);
// }
