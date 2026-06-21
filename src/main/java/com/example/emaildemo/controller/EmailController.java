package com.example.emaildemo.controller;

import com.example.emaildemo.dto.EmailRequest;
import com.example.emaildemo.dto.EmailResponse;
import com.example.emaildemo.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@Tag(name = "Email", description = "Send real SMTP emails")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/health")
    @Operation(summary = "Check whether the email API is running")
    public EmailResponse health() {
        return new EmailResponse("UP", "Email API is running", Instant.now());
    }

    @PostMapping("/send")
    @Operation(summary = "Send an email using configured SMTP settings")
    public ResponseEntity<EmailResponse> sendEmail(@Valid @RequestBody EmailRequest request) {
        emailService.sendEmail(request);
        EmailResponse response = new EmailResponse("SENT", "Email sent successfully to " + request.getTo(), Instant.now());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}