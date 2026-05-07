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
public class JobAlertDto {
    private Long id;
    private String keywords;
    private String location;
    private boolean active;
    private LocalDateTime createdAt;
}
