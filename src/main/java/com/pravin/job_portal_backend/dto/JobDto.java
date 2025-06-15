package com.pravin.job_portal_backend.dto;

import com.pravin.job_portal_backend.enums.ExperienceLevel;
import com.pravin.job_portal_backend.enums.JobStatus;
import com.pravin.job_portal_backend.enums.JobType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {

  private Long id;
  private String title;
  private String location;
  private String salary;
  private String company;

  private String description;
  private List<String> requirements;

  private JobType jobType;
  private ExperienceLevel experienceLevel;
  private JobStatus status;
  private String category;

  private Long postedById;
  private String postedByName;

  private LocalDate postedDate;
  private LocalDate lastDateToApply;
  private LocalDateTime updatedAt;

  private Long postedDaysAgo;
}
