package com.pravin.job_portal_backend.dto.user_dtos;

import com.pravin.job_portal_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    //    private Long id;
    private String name;
    private String email;
    private String password;
    private Role role;
}
