package com.pravin.job_portal_backend.mapper.companys_mapper;

import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;
import com.pravin.job_portal_backend.entity.Company;

public class CompanyMapper {
    public static CompanyDTO toDTO(Company company) {
        if (company == null)
            return null;
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());
        dto.setWebsite(company.getWebsite());
        dto.setLocation(company.getLocation());
        dto.setIndustry(company.getIndustry());
        dto.setContactEmail(company.getContactEmail());
        return dto;
    }

    public static Company toEntity(CompanyDTO dto) {
        if (dto == null)
            return null;
        Company company = new Company();
        company.setId(dto.getId());
        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setWebsite(dto.getWebsite());
        company.setLocation(dto.getLocation());
        company.setIndustry(dto.getIndustry());
        company.setContactEmail(dto.getContactEmail());
        return company;
    }
}
