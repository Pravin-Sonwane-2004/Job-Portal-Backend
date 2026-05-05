package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.InterviewDto;
import com.pravin.job_portal_backend.entity.Interview;

public class InterviewMapper {

  private InterviewMapper() {
  }

  public static InterviewDto toDto(Interview interview) {
    if (interview == null) {
      return null;
    }
    return new InterviewDto(
        interview.getId(),
        interview.getApplication().getId(),
        interview.getCandidate().getId(),
        interview.getCandidate().getName(),
        interview.getEmployer().getId(),
        interview.getEmployer().getName(),
        interview.getScheduledTime(),
        interview.getStatus(),
        interview.getMeetingLink(),
        interview.getNotes());
  }
}
