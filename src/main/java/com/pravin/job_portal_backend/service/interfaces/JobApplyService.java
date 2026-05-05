package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.ApplyJobDto;
import com.pravin.job_portal_backend.dto.ApplyJobRequestDTO;
import com.pravin.job_portal_backend.dto.ApplyJobResponseDTO;
import com.pravin.job_portal_backend.dto.ApplicationProfileDtoAdmin;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface JobApplyService {
    String applyForJob(Long userId, Long jobId);
    String applyForJob(Long userId, Long jobId, ApplyJobRequestDTO request);
    CompletableFuture<List<ApplicationProfileDtoAdmin>> getAllApplicationsWithProfiles();
    List<ApplyJobResponseDTO> getAppliedJobByUserDTO(Long userId);
    String cancelApplication(Long userId, Long jobId);
    List<ApplyJobDto> getAllApplications();
    List<ApplyJobDto> getApplicationsForRecruiter(String recruiterEmail);
    List<ApplyJobDto> getApplicationsForJob(Long jobId);
    List<ApplyJobDto> getApplicationsForRecruiterJob(String recruiterEmail, Long jobId);
    List<ApplyJobDto> getApplicationsByUser(Long userId);
    void deleteApplicationById(Long applicationId);
    void deleteUserApplicationById(String userEmail, Long applicationId);
    void updateApplicationById(Long applicationId, java.util.Map<String, Object> updates);
    void updateUserApplicationById(String userEmail, Long applicationId, java.util.Map<String, Object> updates);
    void updateRecruiterApplicationById(String recruiterEmail, Long applicationId, java.util.Map<String, Object> updates);
}
