package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.CompanyDto;
import com.pravin.job_portal_backend.entity.Company;

public class CompanyMapper {

  private CompanyMapper() {
  }

  public static CompanyDto toDto(Company company, long employeeCount, long jobCount) {
    if (company == null) {
      return null;
    }
    return new CompanyDto(
        company.getId(),
        company.getName(),
        company.getDescription(),
        company.getWebsite(),
        company.getIndustry(),
        company.getLocation(),
        company.getLogoUrl(),
        company.isVerified(),
        employeeCount,
        jobCount);
  }
}
