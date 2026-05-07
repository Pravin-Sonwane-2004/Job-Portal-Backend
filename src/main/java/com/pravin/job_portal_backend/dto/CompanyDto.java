package com.pravin.job_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String description;
    private String website;
    private String industry;
    private String location;
    private String logoUrl;
    private boolean verified;
    private long employeeCount;
    private long jobCount;
}
