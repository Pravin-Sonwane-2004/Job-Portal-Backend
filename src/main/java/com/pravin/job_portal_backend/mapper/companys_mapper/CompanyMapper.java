package com.pravin.job_portal_backend.mapper.companys_mapper;


import com.pravin.job_portal_backend.dto.company_dtos.CompanyDTO;
import com.pravin.job_portal_backend.entity.Company;

public class CompanyMapper {

    // Entity → DTO
    public static CompanyDTO toDTO(Company company) {
        if (company == null) {
            return null;
        }

        return CompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .website(company.getWebsite())
                .location(company.getLocation())
                .industry(company.getIndustry())
                .contactEmail(company.getContactEmail())
                .build();
    }

    // DTO → Entity
    public static Company toEntity(CompanyDTO dto) {
        if (dto == null) {
            return null;
        }

        return Company.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .website(dto.getWebsite())
                .location(dto.getLocation())
                .industry(dto.getIndustry())
                .contactEmail(dto.getContactEmail())
                .build();
    }
}
