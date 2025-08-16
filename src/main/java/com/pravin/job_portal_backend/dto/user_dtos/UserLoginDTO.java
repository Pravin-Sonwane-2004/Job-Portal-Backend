package com.pravin.job_portal_backend.dto;

import com.pravin.job_portal_backend.enums.Role;
import com.pravin.job_portal_backend.enums.ExperienceLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor 
public class UserLoginDTO {
  private Long id;
  private String email;
  private String password;
  private Role role;
  private ExperienceLevel experienceLevel;
}
