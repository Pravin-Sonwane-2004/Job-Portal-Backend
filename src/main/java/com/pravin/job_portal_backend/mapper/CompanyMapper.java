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
    CompanyDto dto = new CompanyDto();
    dto.setId(company.getId());
    dto.setName(company.getName());
    dto.setDescription(company.getDescription());
    dto.setWebsite(company.getWebsite());
    dto.setIndustry(company.getIndustry());
    dto.setLocation(company.getLocation());
    dto.setLogoUrl(company.getLogoUrl());
    dto.setVerified(company.isVerified());
    dto.setEmployeeCount(employeeCount);
    dto.setJobCount(jobCount);
    return dto;
  }
}
