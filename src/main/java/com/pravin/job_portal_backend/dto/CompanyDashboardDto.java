package com.pravin.job_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDashboardDto {
    private CompanyDto company;
    private long employees;
    private long jobs;
    private long applications;
    private String nextAction;
}
