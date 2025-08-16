//package com.pravin.job_portal_backend.mapper.workremaining;
//
//import com.pravin.job_portal_backend.dto.JobDto;
//import com.pravin.job_portal_backend.entity.Job;
//
//public class JobMapper {
//
//  public static JobDto toDto(Job job) {
//    if (job == null)
//      return null;
//
//    return JobDto.builder()
//        .id(job.getId())
//        .title(job.getTitle())
//        .location(job.getLocation())
//        .salary(job.getSalary())
//        .company(job.getCompany())
//        .description(job.getDescription())
//        .requirements(job.getRequirements())
//        .jobType(job.getJobType())
//        .experienceLevel(job.getExperienceLevel())
//        .status(job.getStatus())
//        .category(job.getCategory())
//        .postedById(job.getPostedBy() != null ? job.getPostedBy().getId() : null)
//        .postedByName(job.getPostedBy() != null ? job.getPostedBy().getName() : null)
//        .postedDate(job.getPostedDate())
//        .lastDateToApply(job.getLastDateToApply())
//        .updatedAt(job.getUpdatedAt())
//        .postedDaysAgo(job.postedDaysAgo())
//        .build();
//  }
//
//  public static Job toEntity(JobDto dto) {
//    if (dto == null)
//      return null;
//
//    Job job = new Job();
//    job.setId(dto.getId());
//    job.setTitle(dto.getTitle());
//    job.setLocation(dto.getLocation());
//    job.setSalary(dto.getSalary());
//    job.setCompany(dto.getCompany());
//    job.setDescription(dto.getDescription());
//    job.setRequirements(dto.getRequirements());
//    job.setJobType(dto.getJobType());
//    job.setExperienceLevel(dto.getExperienceLevel());
//    job.setStatus(dto.getStatus());
//    job.setCategory(dto.getCategory());
//    job.setLastDateToApply(dto.getLastDateToApply());
//    // Do not set postedDate and updatedAt manually; handled by @CreationTimestamp /
//    // @UpdateTimestamp
//    return job;
//  }
//}
