package com.pravin.job_portal_backend.service.recruiter;

import com.pravin.job_portal_backend.dto.ricruitors_dtos.RecruiterDTO;

import java.util.List;

public interface RecruiterService {

    RecruiterDTO createRecruiter(RecruiterDTO dto);

    List<RecruiterDTO> getAllRecruiters();

    RecruiterDTO getRecruiterById(Long id);

    RecruiterDTO updateRecruiter(Long id, RecruiterDTO dto);

    void deleteRecruiter(Long id);

    // 🔹 Extra important methods
    List<RecruiterDTO> getRecruitersByCompany(Long companyId);

    RecruiterDTO getRecruiterByUserId(Long userId);
}
