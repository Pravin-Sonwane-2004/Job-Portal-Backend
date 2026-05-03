package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.ProfileInsightsDto;

public interface ProfileInsightsService {
  ProfileInsightsDto getInsightsFor(String email);
}
