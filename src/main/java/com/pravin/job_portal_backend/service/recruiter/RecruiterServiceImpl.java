package com.pravin.job_portal_backend.service.recruiter;

import com.pravin.job_portal_backend.dto.ricruitors_dtos.RecruiterResponseDTO;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.Recruiter;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.enums.CompanyStatus;
import com.pravin.job_portal_backend.exception.BadRequestException;
import com.pravin.job_portal_backend.exception.NotFoundException;
import com.pravin.job_portal_backend.mapper.recruiter_mapper.RecruiterMapper;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import com.pravin.job_portal_backend.repository.RecruiterRepository;
import com.pravin.job_portal_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepo;
    private final UserRepository userRepo;
    private final CompanyRepository companyRepo;

    @Override
    public RecruiterResponseDTO createRecruiter(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        if (recruiterRepo.existsByUser_Id(userId)) {
            throw new BadRequestException("Recruiter already exists for user");
        }

        Recruiter recruiter = Recruiter.builder()
                .user(user)
                .company(null)
                .build();

        recruiter = recruiterRepo.save(recruiter);
        return RecruiterMapper.toResponseDTO(recruiter);
    }

    @Override
    public RecruiterResponseDTO assignRecruiterToCompany(Long recruiterId, Long companyId, Long adminUserId) {
        Recruiter recruiter = recruiterRepo.findById(recruiterId)
                .orElseThrow(() -> new NotFoundException("Recruiter not found: " + recruiterId));
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found: " + companyId));

        if (company.getStatus() != CompanyStatus.APPROVED) {
            throw new BadRequestException("Cannot assign recruiter to a non-approved company");
        }

        recruiter.setCompany(company);
        return RecruiterMapper.toResponseDTO(recruiter);
    }

    @Override
    public RecruiterResponseDTO getRecruiter(Long recruiterId) {
        Recruiter recruiter = recruiterRepo.findById(recruiterId)
                .orElseThrow(() -> new NotFoundException("Recruiter not found: " + recruiterId));
        return RecruiterMapper.toResponseDTO(recruiter);
    }

    @Override
    public List<RecruiterResponseDTO> listByCompany(Long companyId) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found: " + companyId));

        return company.getRecruiters().stream()
                .map(RecruiterMapper::toResponseDTO)
                .toList();
    }

    @Override
    public RecruiterResponseDTO removeRecruiterFromCompany(Long recruiterId, Long adminUserId) {
        Recruiter recruiter = recruiterRepo.findById(recruiterId)
                .orElseThrow(() -> new NotFoundException("Recruiter not found: " + recruiterId));

        recruiter.setCompany(null);
        return RecruiterMapper.toResponseDTO(recruiter);
    }

    @Override
    public void deleteRecruiter(Long recruiterId, Long adminUserId) {
        Recruiter recruiter = recruiterRepo.findById(recruiterId)
                .orElseThrow(() -> new NotFoundException("Recruiter not found: " + recruiterId));
        recruiterRepo.delete(recruiter);
    }

    @Override
    public List<RecruiterResponseDTO> listAllRecruiters() {
        return recruiterRepo.findAll().stream()
                .map(RecruiterMapper::toResponseDTO)
                .toList();
    }

}
