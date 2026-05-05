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
    return new JobAlertDto(alert.getId(), alert.getKeywords(), alert.getLocation(), alert.isActive(), alert.getCreatedAt());
  }
}
