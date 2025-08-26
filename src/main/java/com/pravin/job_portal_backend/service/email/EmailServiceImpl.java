package com.pravin.job_portal_backend.service.email;

import com.pravin.job_portal_backend.exception.EmailSendFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send a welcome email after successful registration.
     */
    @Override
    @Async
    public void sendRegistrationWelcomeEmail(@NonNull String recipient) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(recipient);
            helper.setFrom(fromEmail);
            helper.setSubject("🎉 Welcome to Job Portal!");

            String htmlBody = """
                        <html>
                          <body style="font-family: Arial, sans-serif; line-height:1.6;">
                            <h2 style="color:#2E86C1;">Welcome to Job Portal!</h2>
                            <p>We’re excited to have you onboard 🎉.</p>
                            <p>With your account, you can:</p>
                            <ul>
                                <li>Search and apply for jobs</li>
                                <li>Connect with employers</li>
                                <li>Track your applications easily</li>
                            </ul>
                            <p style="margin-top:20px;">
                               🚀 <b>Start your journey today by logging in to your profile.</b>
                            </p>
                            <hr/>
                            <p style="font-size:12px; color:#888;">
                               This is an automated message, please do not reply.
                            </p>
                          </body>
                        </html>
                    """;

            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            logger.info("Registration welcome email sent to {}", recipient);

        } catch (MessagingException | MailException e) {
            logger.error("Failed to send registration welcome email to {}", recipient, e);
            throw new EmailSendFailedException("Unable to send registration welcome email to " + recipient, e);
        }
    }

    /**
     * Send password reset email with token link.
     */
    @Override
    @Async
    public void sendPasswordResetEmail(String recipient, String resetLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(recipient);
            helper.setFrom(fromEmail);
            helper.setSubject("🔒 Password Reset Request - Job Portal");

            String htmlBody = """
                        <html>
                          <body style="font-family: Arial, sans-serif; line-height:1.6;">
                            <h2 style="color:#D35400;">Password Reset Request</h2>
                            <p>We received a request to reset your password. If this was you, click the button below:</p>
                            <p style="margin:20px 0;">
                               <a href="%s"
                                  style="background-color:#2E86C1; color:white; padding:10px 20px; text-decoration:none; border-radius:5px;">
                                  Reset Password
                               </a>
                            </p>
                            <p>If the button doesn't work, copy and paste this link into your browser:</p>
                            <p style="color:#2E86C1;">%s</p>
                            <p>If you didn’t request a password reset, you can ignore this email.</p>
                            <hr/>
                            <p style="font-size:12px; color:#888;">
                               This is an automated message, please do not reply.
                            </p>
                          </body>
                        </html>
                    """
                    .formatted(resetLink, resetLink);

            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            logger.info("✅ Password reset email sent to {}", recipient);

        } catch (MessagingException | MailException e) {
            logger.error("❌ Failed to send password reset email to {}", recipient, e);
            throw new EmailSendFailedException("Unable to send password reset email to " + recipient, e);
        }
    }
}
