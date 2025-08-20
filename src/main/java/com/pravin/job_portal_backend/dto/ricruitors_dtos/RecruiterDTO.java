package com.pravin.job_portal_backend.dto.ricruitors_dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruiterDTO {
    private Long id;
    private Long userId;
    private Long companyId;
}
