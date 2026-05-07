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
    InterviewDto dto = new InterviewDto();
    dto.setId(interview.getId());
    dto.setApplicationId(interview.getApplication().getId());
    dto.setCandidateId(interview.getCandidate().getId());
    dto.setCandidateName(interview.getCandidate().getName());
    dto.setEmployerId(interview.getEmployer().getId());
    dto.setEmployerName(interview.getEmployer().getName());
    dto.setScheduledTime(interview.getScheduledTime());
    dto.setStatus(interview.getStatus());
    dto.setMeetingLink(interview.getMeetingLink());
    dto.setNotes(interview.getNotes());
    return dto;
  }
}
