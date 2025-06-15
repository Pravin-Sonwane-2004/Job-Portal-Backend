package com.pravin.job_portal_backend.service.impl;

import org.springframework.stereotype.Service;

import com.pravin.job_portal_backend.entity.EmailRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailServiceImpl implements com.pravin.job_portal_backend.service.interfaces.EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailRequest email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());
        message.setFrom("pravinson222@gmail.com"); // Explicitly set the from address
        mailSender.send(message);
    }
}
