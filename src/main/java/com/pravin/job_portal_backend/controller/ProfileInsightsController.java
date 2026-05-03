package com.pravin.job_portal_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.ProfileInsightsDto;
import com.pravin.job_portal_backend.service.interfaces.ProfileInsightsService;

@RestController
@RequestMapping("/api/profile-insights")
public class ProfileInsightsController {

  private final ProfileInsightsService profileInsightsService;

  public ProfileInsightsController(ProfileInsightsService profileInsightsService) {
    this.profileInsightsService = profileInsightsService;
  }

  @GetMapping("/me")
  public ResponseEntity<ProfileInsightsDto> getMyInsights(Authentication authentication) {
    return ResponseEntity.ok(profileInsightsService.getInsightsFor(authentication.getName()));
  }
}
