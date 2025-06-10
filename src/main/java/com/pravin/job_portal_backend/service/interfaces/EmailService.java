package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.entity.Email;

public interface EmailService {
    void sendEmail(Email email);
}
