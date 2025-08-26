package com.pravin.job_portal_backend.service.company;


import java.util.List;

import com.pravin.job_portal_backend.dto.company_dtos.*;

public interface CompanyService {
    CompanyResponseDTO createCompanyAsRecruiter(Long recruiterId, CompanyCreateDTO dto);

    CompanyResponseDTO approveCompany(Long companyId, Long adminUserId);

    CompanyResponseDTO rejectCompany(Long companyId, Long adminUserId, String reason);

    CompanyResponseDTO updateCompany(Long companyId, Long actorUserId, CompanyUpdateDTO dto);

    CompanyResponseDTO getCompany(Long id);

    List<CompanyResponseDTO> listPending();

    List<CompanyResponseDTO> listApproved();

    List<CompanyResponseDTO> listByRecruiter(Long recruiterId);

    void deleteCompany(Long companyId, Long adminUserId);

    List<CompanyResponseDTO> searchCompanies(String keyword);

    List<CompanyResponseDTO> listAll();

}
