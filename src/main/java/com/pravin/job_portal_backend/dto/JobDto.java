package com.pravin.job_portal_backend.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private Long id;
    private String title;
    private String location;
    private String salary;
    private String company;
    private LocalDate postedDate;
    private Long postedDaysAgo;
}
