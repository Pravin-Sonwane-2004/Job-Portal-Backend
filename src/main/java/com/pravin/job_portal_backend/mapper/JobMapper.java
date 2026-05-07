package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.JobDto;
import com.pravin.job_portal_backend.entity.Job;
import com.pravin.job_portal_backend.enums.JobStatus;

public class JobMapper {

  public static JobDto toDto(Job job) {
    if (job == null)
      return null;

    JobDto dto = new JobDto();
    dto.setId(job.getId());
    dto.setTitle(job.getTitle());
    dto.setLocation(job.getLocation());
    dto.setSalary(job.getSalary());
    dto.setCompany(job.getCompany());
    dto.setDescription(job.getDescription());
    dto.setRequirements(job.getRequirements());
    dto.setJobType(job.getJobType());
    dto.setExperienceLevel(job.getExperienceLevel());
    dto.setStatus(job.getStatus());
    dto.setCategory(job.getCategory());
    dto.setPostedById(job.getPostedBy() != null ? job.getPostedBy().getId() : null);
    dto.setPostedByName(job.getPostedBy() != null ? job.getPostedBy().getName() : null);
    dto.setPostedDate(job.getPostedDate());
    dto.setLastDateToApply(job.getLastDateToApply());
    dto.setUpdatedAt(job.getUpdatedAt());
    dto.setPostedDaysAgo(job.postedDaysAgo());
    return dto;
  }

  public static Job toEntity(JobDto dto) {
    if (dto == null)
      return null;

    Job job = new Job();
    job.setId(dto.getId());
    job.setTitle(dto.getTitle());
    job.setLocation(dto.getLocation());
    job.setSalary(dto.getSalary());
    job.setCompany(dto.getCompany());
    job.setDescription(dto.getDescription());
    job.setRequirements(dto.getRequirements());
    job.setJobType(dto.getJobType());
    job.setExperienceLevel(dto.getExperienceLevel());
    job.setStatus(dto.getStatus() != null ? dto.getStatus() : JobStatus.OPEN);
    job.setCategory(dto.getCategory());
    job.setLastDateToApply(dto.getLastDateToApply());
    // Do not set postedDate and updatedAt manually; handled by @CreationTimestamp /
    // @UpdateTimestamp
    return job;
  }
}
