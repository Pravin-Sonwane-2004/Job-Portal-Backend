package com.pravin.job_portal_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pravin.job_portal_backend.entity.Company;
import com.pravin.job_portal_backend.entity.CompanyReview;

public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Long> {
  List<CompanyReview> findByCompanyOrderByCreatedAtDesc(Company company);
}
