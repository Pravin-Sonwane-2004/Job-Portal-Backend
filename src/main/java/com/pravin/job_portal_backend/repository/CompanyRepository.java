package com.pravin.job_portal_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.enums.CompanyStatus;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByStatus(CompanyStatus status);

    boolean existsByNameIgnoreCase(String name);
}
