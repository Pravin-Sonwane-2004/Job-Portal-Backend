package com.pravin.job_portal_backend.service.interfaces;

public interface PasswordResetService {
  void requestPasswordReset(String email);

  void resetPassword(String token, String newPassword);
}
