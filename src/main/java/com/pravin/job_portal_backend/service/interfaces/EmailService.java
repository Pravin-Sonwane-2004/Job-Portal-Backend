package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.entity.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest email);
}
