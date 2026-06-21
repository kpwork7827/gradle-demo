package com.example.emaildemo.service;

import com.example.emaildemo.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String defaultFrom;

    public EmailService(JavaMailSender mailSender, @Value("${app.mail.default-from}") String defaultFrom) {
        this.mailSender = mailSender;
        this.defaultFrom = defaultFrom;
    }

    public void sendEmail(EmailRequest request) {
        if (!StringUtils.hasText(defaultFrom)) {
            throw new IllegalStateException("MAIL_FROM or SMTP_USERNAME must be configured before sending email");
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(defaultFrom);
            helper.setTo(request.getTo());

            if (StringUtils.hasText(request.getCc())) {
                helper.setCc(request.getCc());
            }

            helper.setSubject(request.getSubject());
            helper.setText(request.getBody(), request.isHtml());
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            throw new IllegalStateException("Failed to prepare email message", ex);
        }
    }
}