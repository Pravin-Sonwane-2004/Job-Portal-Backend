package com.pravin.job_portal_backend.dto.company_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for Company entity.
 */
@Data
public class CompanyDTO {
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 512)
    private String website;

    @Size(max = 1024)
    private String description;

    @Size(max = 255)
    private String location;

    @Size(max = 255)
    private String industry;

    @Size(max = 255)
    private String contactEmail;
}
