package com.pravin.job_portal_backend.dto.company_dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyRequestDTO {
    private String name;
    private String description;
    private String website;
    private String location;
    private String industry;
    private String contactEmail;
}
