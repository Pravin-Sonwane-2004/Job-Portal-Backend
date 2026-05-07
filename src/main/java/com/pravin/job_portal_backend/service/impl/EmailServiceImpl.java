package com.pravin.job_portal_backend.service.impl;

import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.entity.EmailRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Sends plain text emails through Spring's JavaMailSender.
 *
 * Flow:
 * Controller/service creates EmailRequest -> EmailServiceImpl builds a mail
 * message -> JavaMailSender sends it using the mail settings from application
 * properties or environment variables.
 */
@Service
public class EmailServiceImpl implements com.pravin.job_portal_backend.service.interfaces.EmailService {
    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${app.mail.from:${spring.mail.username:}}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendEmail(EmailRequest email) {
        // SimpleMailMessage is enough here because this project sends plain text email.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());

        // "from" is optional so local/dev environments can run without extra mail config.
        if (fromAddress != null && !fromAddress.isBlank()) {
            message.setFrom(fromAddress);
        }
        mailSender.send(message);
    }
}
