package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.ApplyJobDto;
import com.pravin.job_portal_backend.entity.ApplyJob;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.entity.User;

/**
 * Converts ApplyJob database entities into DTOs used by controllers.
 *
 * Mapping is kept out of services so business logic stays readable.
 */

public class ApplyJobMapper {
    /**
     * Entity -> DTO for API responses.
     * Nested user/job objects are delegated to their own mappers.
     */
    public static ApplyJobDto toDto(ApplyJob applyJob) {
        if (applyJob == null) return null;

        User user = applyJob.getUser();
        Job job = applyJob.getJob();

        ApplyJobDto dto = new ApplyJobDto();
        dto.setId(applyJob.getId());
        dto.setUserId(user != null ? user.getId() : null);
        dto.setJobId(job != null ? job.getId() : null);
        dto.setAppliedAt(applyJob.getAppliedAt());
        dto.setUpdatedAt(applyJob.getUpdatedAt());
        dto.setStatus(applyJob.getStatus());
        dto.setRecruiterRemarks(applyJob.getRecruiterRemarks());
        dto.setResumeLink(applyJob.getResumeLink());
        dto.setCoverLetter(applyJob.getCoverLetter());
        dto.setPhoneNumber(applyJob.getPhoneNumber());
        dto.setLinkedinUrl(applyJob.getLinkedinUrl());
        dto.setPortfolioUrl(applyJob.getPortfolioUrl());
        dto.setExpectedSalary(applyJob.getExpectedSalary());
        dto.setNoticePeriod(applyJob.getNoticePeriod());
        dto.setAppliedFromIp(applyJob.getAppliedFromIp());
        dto.setSource(applyJob.getSource());
        dto.setUserAgent(applyJob.getUserAgent());
        dto.setUser(UserMapper.toDto(user));
        dto.setJob(JobMapper.toDto(job));
        return dto;
    }

    /**
     * DTO -> Entity for internal use.
     * User and Job relationships are intentionally set in the service because
     * they must be loaded and validated from the database first.
     */
    public static ApplyJob toEntity(ApplyJobDto dto) {
        if (dto == null) return null;
        ApplyJob applyJob = new ApplyJob();
        applyJob.setId(dto.getId());
        applyJob.setAppliedAt(dto.getAppliedAt());
        applyJob.setStatus(dto.getStatus());
        applyJob.setRecruiterRemarks(dto.getRecruiterRemarks());
        applyJob.setResumeLink(dto.getResumeLink());
        applyJob.setCoverLetter(dto.getCoverLetter());
        applyJob.setPhoneNumber(dto.getPhoneNumber());
        applyJob.setLinkedinUrl(dto.getLinkedinUrl());
        applyJob.setPortfolioUrl(dto.getPortfolioUrl());
        applyJob.setExpectedSalary(dto.getExpectedSalary());
        applyJob.setNoticePeriod(dto.getNoticePeriod());
        applyJob.setAppliedFromIp(dto.getAppliedFromIp());
        applyJob.setSource(dto.getSource());
        applyJob.setUserAgent(dto.getUserAgent());
        return applyJob;
    }
}
