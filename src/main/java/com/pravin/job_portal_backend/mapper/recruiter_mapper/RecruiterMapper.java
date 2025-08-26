package com.pravin.job_portal_backend.mapper.recruiter_mapper;

import com.pravin.job_portal_backend.dto.ricruitors_dtos.*;
import com.pravin.job_portal_backend.entity.Recruiter;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.User;

public class RecruiterMapper {

    // Convert Entity → Response DTO
    public static RecruiterResponseDTO toResponseDTO(Recruiter recruiter) {
        if (recruiter == null)
            return null;

        return RecruiterResponseDTO.builder()
                .id(recruiter.getId())
                .userId(recruiter.getUser() != null ? recruiter.getUser().getId() : null)
                .companyId(recruiter.getCompany() != null ? recruiter.getCompany().getId() : null)
                .build();
    }

    // Convert Create DTO → Entity
    public static Recruiter toEntity(RecruiterCreateDTO dto, User user, Company company) {
        if (dto == null)
            return null;

        return Recruiter.builder()
                .user(user)
                .company(company)
                .build();
    }

    // Update existing Entity with Update DTO
    public static void updateEntity(Recruiter recruiter, RecruiterUpdateDTO dto, Company company) {
        if (company != null)
            recruiter.setCompany(company);
    }
}
