  package com.pravin.job_portal_backend.mapper;

  import com.pravin.job_portal_backend.dto.ApplyJobDto;
  import com.pravin.job_portal_backend.dto.JobDto;
  import com.pravin.job_portal_backend.dto.UserDto;
  import com.pravin.job_portal_backend.entity.ApplyJob;
  import com.pravin.job_portal_backend.entity.Job;
  import com.pravin.job_portal_backend.entity.User;

  public class ApplyJobMapper {
      public static ApplyJobDto toDto(ApplyJob applyJob) {
          if (applyJob == null) return null;
          User user = applyJob.getUser();
          Job job = applyJob.getJob();

          UserDto userDto = null;
          if (user != null) {
              userDto = UserDto.builder()
                  .id(user.getId())
                  .email(user.getEmail())
                  .name(user.getName())
                  .avatarUrl(user.getAvatarUrl())
                  .designation(user.getDesignation())
                  .verified(user.getVerified())
                  .location(user.getLocation())
                  .bio(user.getBio())
                  .phoneNumber(user.getPhoneNumber())
                  .linkedinUrl(user.getLinkedinUrl())
                  .jobRole(user.getJobRole())
                  .role(user.getRole() != null ? user.getRole().toString() : null)
                  .skills(user.getSkills())
                  .build();
          }

          JobDto jobDto = null;
          if (job != null) {
              jobDto = JobDto.builder()
                  .id(job.getId())
                  .title(job.getTitle())
                  .company(job.getCompany())
                  .location(job.getLocation())
                  .salary(job.getSalary())
                  // add other fields as needed
                  .build();
          }

          return ApplyJobDto.builder()
                  .id(applyJob.getId())
                  .userId(user != null ? user.getId() : null)
                  .jobId(job != null ? job.getId() : null)
                  .appliedAt(applyJob.getAppliedAt())
                  .updatedAt(applyJob.getUpdatedAt())
                  .status(applyJob.getStatus())
                  .recruiterRemarks(applyJob.getRecruiterRemarks())
                  .resumeLink(applyJob.getResumeLink())
                  .coverLetter(applyJob.getCoverLetter())
                  .phoneNumber(applyJob.getPhoneNumber())
                  .linkedinUrl(applyJob.getLinkedinUrl())
                  .portfolioUrl(applyJob.getPortfolioUrl())
                  .expectedSalary(applyJob.getExpectedSalary())
                  .noticePeriod(applyJob.getNoticePeriod())
                  .appliedFromIp(applyJob.getAppliedFromIp())
                  .source(applyJob.getSource())
                  .userAgent(applyJob.getUserAgent())
                  .user(userDto)
                  .job(jobDto)
                  .build();
      }

      public static ApplyJob toEntity(ApplyJobDto dto) {
          if (dto == null) return null;
          ApplyJob applyJob = new ApplyJob();
          applyJob.setId(dto.getId());
          // User and Job should be set in service if needed
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
