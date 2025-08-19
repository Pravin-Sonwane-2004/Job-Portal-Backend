package com.pravin.job_portal_backend.dto.job_dtos;

import com.pravin.job_portal_backend.enums.ExperienceLevel;
import com.pravin.job_portal_backend.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String location;

    @NotNull
    private Double minSalary;

    @NotNull
    private Double maxSalary;

    private String currency = "INR";

    @NotBlank
    @Size(max = 255)
    private String company;

    @NotBlank
    private String description;

    private List<String> requirements;

    @NotNull
    private JobType jobType;

    @NotNull
    private ExperienceLevel experienceLevel;

    private String category;

    private LocalDate lastDateToApply;
}
