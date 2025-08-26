package com.pravin.job_portal_backend.dto.ricruitors_dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecruiterUpdateDTO {
    private Long companyId; // recruiter can switch company (if business rules allow)
}
