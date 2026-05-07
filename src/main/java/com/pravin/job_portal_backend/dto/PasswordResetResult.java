package com.pravin.job_portal_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetResult {
    private boolean userFound;
    private boolean emailSent;
    private String resetLink;
}
