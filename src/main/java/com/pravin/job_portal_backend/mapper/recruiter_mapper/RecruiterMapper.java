package com.pravin.job_portal_backend.mapper.recruiter_mapper;


import com.pravin.job_portal_backend.entity.Recruiter;
import com.pravin.job_portal_backend.dto.ricruitors_dtos.RecruiterDTO;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.User;

public class RecruiterMapper {

    public static RecruiterDTO toDTO(Recruiter recruiter) {
        if (recruiter == null)
            return null;
        return RecruiterDTO.builder()
                .id(recruiter.getId())
                .userId(recruiter.getUser() != null ? recruiter.getUser().getId() : null)
                .companyId(recruiter.getCompany() != null ? recruiter.getCompany().getId() : null)
                .build();
    }

    public static Recruiter toEntity(RecruiterDTO dto, User user, Company company) {
        if (dto == null)
            return null;
        return Recruiter.builder()
                .id(dto.getId())
                .user(user)
                .company(company)
                .build();
    }
}
