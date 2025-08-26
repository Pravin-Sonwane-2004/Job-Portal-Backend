package com.pravin.job_portal_backend.service.recruiter;

import java.util.List;

import com.pravin.job_portal_backend.dto.ricruitors_dtos.RecruiterResponseDTO;

public interface RecruiterService {
    RecruiterResponseDTO createRecruiter(Long userId);

    RecruiterResponseDTO assignRecruiterToCompany(Long recruiterId, Long companyId, Long adminUserId);

    RecruiterResponseDTO getRecruiter(Long recruiterId);

    List<RecruiterResponseDTO> listByCompany(Long companyId);

    RecruiterResponseDTO removeRecruiterFromCompany(Long recruiterId, Long adminUserId);

    void deleteRecruiter(Long recruiterId, Long adminUserId);

    List<RecruiterResponseDTO> listAllRecruiters();

}
