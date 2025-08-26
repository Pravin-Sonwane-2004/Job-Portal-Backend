package com.pravin.job_portal_backend.service.company;



import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.dto.company_dtos.*;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.Recruiter;
import com.pravin.job_portal_backend.enums.CompanyStatus;
import com.pravin.job_portal_backend.exception.BadRequestException;
import com.pravin.job_portal_backend.mapper.company_mapper.CompanyMapper;
import com.pravin.job_portal_backend.exception.NotFoundException;

import com.pravin.job_portal_backend.repository.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepo;
    private final RecruiterRepository recruiterRepo;
    private final UserRepository userRepo;

    @Override
    public CompanyResponseDTO createCompanyAsRecruiter(Long recruiterId, CompanyCreateDTO dto) {
        Recruiter recruiter = recruiterRepo.findById(recruiterId)
                .orElseThrow(() -> new NotFoundException("Recruiter not found: " + recruiterId));

        // Guard: company duplicates
        if (companyRepo.existsByNameIgnoreCase(dto.getName())) {
            throw new BadRequestException("Company with this name already exists");
        }

        Company company = CompanyMapper.toEntity(dto);
        company.setStatus(CompanyStatus.PENDING);

        // Persist company
        company = companyRepo.save(company);

        // Optional: link creator immediately (can still be pending)
        recruiter.setCompany(company);

        return CompanyMapper.toResponseDTO(company);
    }

    @Override
    public CompanyResponseDTO approveCompany(Long companyId, Long adminUserId) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found: " + companyId));

        if (company.getStatus() == CompanyStatus.APPROVED) {
            return CompanyMapper.toResponseDTO(company);
        }
        if (company.getStatus() == CompanyStatus.REJECTED) {
            throw new BadRequestException("Cannot approve a rejected company");
        }

        company.setStatus(CompanyStatus.APPROVED);
        return CompanyMapper.toResponseDTO(company);
    }

    @Override
    public CompanyResponseDTO rejectCompany(Long companyId, Long adminUserId, String reason) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found: " + companyId));

        if (company.getStatus() == CompanyStatus.APPROVED) {
            throw new BadRequestException("Cannot reject an approved company");
        }

        company.setStatus(CompanyStatus.REJECTED);
        // (Optional) store reason in a separate audit table/log
        return CompanyMapper.toResponseDTO(company);
    }

    @Override
    public CompanyResponseDTO updateCompany(Long companyId, Long actorUserId, CompanyUpdateDTO dto) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found: " + companyId));

        try {
            CompanyMapper.updateEntity(company, dto);
            // @Version on Company enforces optimistic locking here
            return CompanyMapper.toResponseDTO(company);
        } catch (OptimisticLockingFailureException e) {
            throw new BadRequestException("Company update conflict, please retry");
        }
    }

    @Override
    public CompanyResponseDTO getCompany(Long id) {
        return companyRepo.findById(id)
                .map(CompanyMapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("Company not found: " + id));
    }

    @Override
    public List<CompanyResponseDTO> listPending() {
        return companyRepo.findByStatus(CompanyStatus.PENDING).stream()
                .map(CompanyMapper::toResponseDTO).collect(toList());
    }

    @Override
    public List<CompanyResponseDTO> listApproved() {
        return companyRepo.findByStatus(CompanyStatus.APPROVED).stream()
                .map(CompanyMapper::toResponseDTO).collect(toList());
    }

    @Override
    public List<CompanyResponseDTO> listByRecruiter(Long recruiterId) {
        Recruiter recruiter = recruiterRepo.findById(recruiterId)
                .orElseThrow(() -> new NotFoundException("Recruiter not found: " + recruiterId));

        Company company = recruiter.getCompany();
        if (company == null)
            return List.of();
        return List.of(CompanyMapper.toResponseDTO(company));
    }

    @Override
    public void deleteCompany(Long companyId, Long adminUserId) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found: " + companyId));

        companyRepo.delete(company);
    }

    @Override
    public List<CompanyResponseDTO> searchCompanies(String keyword) {
        return companyRepo.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        c.getIndustry().toLowerCase().contains(keyword.toLowerCase()) ||
                        c.getLocation().toLowerCase().contains(keyword.toLowerCase()))
                .map(CompanyMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<CompanyResponseDTO> listAll() {
        return companyRepo.findAll().stream()
                .map(CompanyMapper::toResponseDTO)
                .toList();
    }

}
