package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.CompanyReviewDto;
import com.pravin.job_portal_backend.entity.CompanyReview;

public class CompanyReviewMapper {

  private CompanyReviewMapper() {
  }

  public static CompanyReviewDto toDto(CompanyReview review) {
    if (review == null) {
      return null;
    }
    return new CompanyReviewDto(
        review.getId(),
        review.getCompany().getId(),
        review.getCompany().getName(),
        review.getUser().getId(),
        review.getUser().getName(),
        review.getContent(),
        review.getRating(),
        review.getCreatedAt());
  }
}
