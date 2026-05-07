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
public class InterviewDto {
    private Long id;
    private Long applicationId;
    private Long candidateId;
    private String candidateName;
    private Long employerId;
    private String employerName;
    private LocalDateTime scheduledTime;
    private String status;
    private String meetingLink;
    private String notes;
}
