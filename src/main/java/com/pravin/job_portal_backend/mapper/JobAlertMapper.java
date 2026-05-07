package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.JobAlertDto;
import com.pravin.job_portal_backend.entity.JobAlert;

public class JobAlertMapper {

  private JobAlertMapper() {
  }

  public static JobAlertDto toDto(JobAlert alert) {
    if (alert == null) {
      return null;
    }
    JobAlertDto dto = new JobAlertDto();
    dto.setId(alert.getId());
    dto.setKeywords(alert.getKeywords());
    dto.setLocation(alert.getLocation());
    dto.setActive(alert.isActive());
    dto.setCreatedAt(alert.getCreatedAt());
    return dto;
  }
}
