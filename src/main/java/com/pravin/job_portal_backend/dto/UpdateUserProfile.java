package com.pravin.job_portal_backend.dto;

import lombok.Data;
import java.util.List;

import com.pravin.job_portal_backend.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
public class UpdateUserProfile {
    @Size(max = 50)
    private String name; // ✅ renamed from usersName
    @Email
    private String email;
    @Size(max = 100)
    private String location;
    @Size(max = 500)
    private String bio;
    private String phoneNumber;
    private String githubURL;
    private String linkedinUrl;
    private List<String> skills;
    private String avatarUrl;
    private String designation;
    private Boolean verified;
    private Role role;
    // private String jobRole;
  // private List<ApplyJob> appliedJobs;
    // private String password;
    // public String getPassword() {
    //     return password;
    // }
    // public void setPassword(String password) {
    //     this.password = password;
    // }
}
