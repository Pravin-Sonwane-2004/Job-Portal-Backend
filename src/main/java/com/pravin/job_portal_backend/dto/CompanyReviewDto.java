package com.pravin.job_portal_backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyReviewDto {
    private Long id;
    private Long companyId;
    private String companyName;
    private Long userId;
    private String userName;
    private String content;
    private int rating;
    private LocalDateTime createdAt;
}
