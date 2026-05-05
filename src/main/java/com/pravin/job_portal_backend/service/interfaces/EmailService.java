package com.pravin.job_portal_backend.service.interfaces;

import com.pravin.job_portal_backend.entity.EmailRequest;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    void sendEmail(EmailRequest email);
    CompletableFuture<Boolean> sendEmailAsync(EmailRequest email);
}
