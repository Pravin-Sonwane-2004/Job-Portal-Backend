package com.pravin.job_portal_backend.dto;


import com.pravin.job_portal_backend.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
  private String email;
  private String password;
  private Role role; 
}
