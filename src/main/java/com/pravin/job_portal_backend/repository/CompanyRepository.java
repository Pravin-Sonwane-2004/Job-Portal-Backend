package com.pravin.job_portal_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
  Optional<Company> findByNameIgnoreCase(String name);
}
