package com.pravin.job_portal_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanySignupRequest {
    @NotBlank
    private String companyName;
    private String description;
    private String website;
    private String industry;
    private String location;
    private String logoUrl;

    @NotBlank
    private String ownerName;

    @Email
    @NotBlank
    private String ownerEmail;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password;
}
