package com.example.emaildemo.dto;

import java.time.Instant;

public class EmailResponse {
    private final String status;
    private final String message;
    private final Instant timestamp;

    public EmailResponse(String status, String message, Instant timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}