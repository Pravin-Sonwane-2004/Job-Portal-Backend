package com.pravin.job_portal_backend.mapper.company_mapper;

import com.pravin.job_portal_backend.dto.company_dtos.*;
import com.pravin.job_portal_backend.entity.Company;

public final class CompanyMapper {

    private CompanyMapper() {
    }

    // === RequestDTO → Entity ===
    public static Company toEntity(CompanyRequestDTO dto) {
        if (dto == null)
            return null;

        return Company.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .website(dto.getWebsite())
                .location(dto.getLocation())
                .industry(dto.getIndustry())
                .contactEmail(dto.getContactEmail())
                .build();
    }

    // === Entity → ResponseDTO ===
    public static CompanyResponseDTO toResponseDto(Company company) {
        if (company == null)
            return null;

        return CompanyResponseDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .website(company.getWebsite())
                .location(company.getLocation())
                .industry(company.getIndustry())
                .contactEmail(company.getContactEmail())
                .build();
    }

    // === Update Entity with DTO ===
    public static void updateEntityFromDto(CompanyRequestDTO dto, Company company) {
        if (dto == null || company == null)
            return;

        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setWebsite(dto.getWebsite());
        company.setLocation(dto.getLocation());
        company.setIndustry(dto.getIndustry());
        company.setContactEmail(dto.getContactEmail());
    }
}
