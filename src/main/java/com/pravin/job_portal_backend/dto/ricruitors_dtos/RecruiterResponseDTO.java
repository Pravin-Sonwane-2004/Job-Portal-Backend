package com.pravin.job_portal_backend.dto.ricruitors_dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruiterResponseDTO {
    private Long id;
    private Long userId;
    private Long companyId;
}
