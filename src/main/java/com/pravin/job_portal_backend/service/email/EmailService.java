package com.pravin.job_portal_backend.service.email;

import org.springframework.lang.NonNull;

public interface EmailService {

    void sendRegistrationWelcomeEmail(@NonNull String recipient);
}

