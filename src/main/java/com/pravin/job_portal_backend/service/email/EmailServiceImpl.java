package com.pravin.job_portal_backend.service.email;


public class EmailService implements EmailServiceImpl {

        private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

        private final JavaMailSender mailSender;

        @Value("${spring.mail.username}")
        private String fromEmail;

        public EmailServiceImpl(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }

        /**
         * Send a plain text email.
         */
        @Async
        public void sendPlainEmail(EmailRequest email) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email.getTo());
                message.setSubject(email.getSubject());
                message.setText(email.getBody());
                message.setFrom(fromEmail);

                mailSender.send(message);
                logger.info("Plain text email sent to {}", email.getTo());

            } catch (MailException e) {
                logger.error("Failed to send email to {}", email.getTo(), e);
                throw new EmailSendFailedException("Unable to send email to " + email.getTo());
            }
        }

        /**
         * Send an HTML email.
         */
        @Async
        public void sendHtmlEmail(EmailRequest email) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setTo(email.getTo());
                helper.setSubject(email.getSubject());
                helper.setText(email.getBody(), true); // true = HTML content
                helper.setFrom(fromEmail);

                mailSender.send(mimeMessage);
                logger.info("HTML email sent to {}", email.getTo());

            } catch (MessagingException | MailException e) {
                logger.error("Failed to send HTML email to {}", email.getTo(), e);
                throw new EmailSendFailedException("Unable to send HTML email to " + email.getTo());
            }
        }
    }