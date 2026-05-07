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
    CompanyReviewDto dto = new CompanyReviewDto();
    dto.setId(review.getId());
    dto.setCompanyId(review.getCompany().getId());
    dto.setCompanyName(review.getCompany().getName());
    dto.setUserId(review.getUser().getId());
    dto.setUserName(review.getUser().getName());
    dto.setContent(review.getContent());
    dto.setRating(review.getRating());
    dto.setCreatedAt(review.getCreatedAt());
    return dto;
  }
}
