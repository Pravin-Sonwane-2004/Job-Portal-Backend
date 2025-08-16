package com.pravin.job_portal_backend.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRequest {

  @NotBlank
  @Email
  @Size(max = 255)
  private String recipient;

  @NotBlank
  @Size(max = 150) // limit subject length
  private String subject;

  @NotBlank
  @Size(max = 5000) // reasonable upper limit for body
  private String body;
}
