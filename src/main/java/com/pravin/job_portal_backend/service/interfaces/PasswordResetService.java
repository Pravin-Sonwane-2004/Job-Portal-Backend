package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.dto.PasswordResetResult;

public interface PasswordResetService {
  PasswordResetResult requestPasswordReset(String email);

  void resetPassword(String token, String newPassword);
}
