package com.pravin.job_portal_backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJobDto {
    private Long id;
    private Long userId;
    private Long jobId;
    private LocalDateTime savedAt;
}
