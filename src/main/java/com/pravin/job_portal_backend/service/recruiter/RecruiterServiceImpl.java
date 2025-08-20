package com.pravin.job_portal_backend.service.recruiter;

import com.pravin.job_portal_backend.dto.ricruitors_dtos.RecruiterDTO;
import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.Recruiter;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.CompanyRepository;
import com.pravin.job_portal_backend.repository.RecruiterRepository;
import com.pravin.job_portal_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    public RecruiterDTO createRecruiter(RecruiterDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + dto.getUserId()));
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id " + dto.getCompanyId()));

        Recruiter recruiter = Recruiter.builder()
                .user(user)
                .company(company)
                .build();

        return mapToDTO(recruiterRepository.save(recruiter));
    }

    @Override
    public List<RecruiterDTO> getAllRecruiters() {
        return recruiterRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecruiterDTO getRecruiterById(Long id) {
        return recruiterRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with id " + id));
    }

    @Override
    public RecruiterDTO updateRecruiter(Long id, RecruiterDTO dto) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with id " + id));

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + dto.getUserId()));
            recruiter.setUser(user);
        }

        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found with id " + dto.getCompanyId()));
            recruiter.setCompany(company);
        }

        return mapToDTO(recruiterRepository.save(recruiter));
    }

    @Override
    public void deleteRecruiter(Long id) {
        recruiterRepository.deleteById(id);
    }

    // 🔹 Extra methods

    @Override
    public List<RecruiterDTO> getRecruitersByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id " + companyId));
        return company.getRecruiters().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RecruiterDTO getRecruiterByUserId(Long userId) {
        Recruiter recruiter = recruiterRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found for userId " + userId));
        return mapToDTO(recruiter);
    }

    // 🔹 Mapper
    private RecruiterDTO mapToDTO(Recruiter recruiter) {
        return RecruiterDTO.builder()
                .id(recruiter.getId())
                .userId(recruiter.getUser() != null ? recruiter.getUser().getId() : null)
                .companyId(recruiter.getCompany() != null ? recruiter.getCompany().getId() : null)
                .build();
    }
}
