package com.pravin.job_portal_backend.service.authService;

public interface ForgotPasswordService {

    /**
     * Generate reset token and send reset email
     */
    
    String forgotPassword(String email);

    /**
     * Reset password using token
     */
    void resetPassword(String token, String newPassword);
}
