package com.pravin.job_portal_backend.mapper.company_mapper;



import com.pravin.job_portal_backend.dto.company_dtos.*;
import com.pravin.job_portal_backend.entity.Company;

public class CompanyMapper {

    // Convert Entity → Response DTO
    public static CompanyResponseDTO toResponseDTO(Company company) {
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

    // Convert Create DTO → Entity
    public static Company toEntity(CompanyCreateDTO dto) {
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

    // Update existing Entity with Update DTO
    public static void updateEntity(Company company, CompanyUpdateDTO dto) {
        if (dto.getDescription() != null)
            company.setDescription(dto.getDescription());
        if (dto.getWebsite() != null)
            company.setWebsite(dto.getWebsite());
        if (dto.getLocation() != null)
            company.setLocation(dto.getLocation());
        if (dto.getIndustry() != null)
            company.setIndustry(dto.getIndustry());
        if (dto.getContactEmail() != null)
            company.setContactEmail(dto.getContactEmail());
    }
}
