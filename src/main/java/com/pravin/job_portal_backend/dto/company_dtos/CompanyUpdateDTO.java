package com.pravin.job_portal_backend.dto.company_dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyUpdateDTO {
    private String description;
    private String website;
    private String location;
    private String industry;
    private String contactEmail;
}
