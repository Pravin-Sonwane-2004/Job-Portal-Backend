package com.pravin.job_portal_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanySignupRequest(
    @NotBlank String companyName,
    String description,
    String website,
    String industry,
    String location,
    String logoUrl,
    @NotBlank String ownerName,
    @Email @NotBlank String ownerEmail,
    @NotBlank @Size(min = 8, max = 255) String password) {
}
