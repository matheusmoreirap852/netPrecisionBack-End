package com.netprecision.taskmanager.domain.model;

import java.time.OffsetDateTime;

public class ContactMessage {

    private final Long id;
    private final String name;
    private final String email;
    private final String subject;
    private final String message;
    private final OffsetDateTime createdAt;

    public ContactMessage(
            Long id,
            String name,
            String email,
            String subject,
            String message,
            OffsetDateTime createdAt
    ) {
        this.id = id;
        this.name = normalizeRequired(name, "Name is required");
        this.email = normalizeEmail(email);
        this.subject = normalizeRequired(subject, "Subject is required");
        this.message = normalizeMessage(message);
        this.createdAt = createdAt == null ? OffsetDateTime.now() : createdAt;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    public String subject() {
        return subject;
    }

    public String message() {
        return message;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    private String normalizeRequired(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private String normalizeEmail(String email) {
        String normalizedEmail = normalizeRequired(email, "Email is required").toLowerCase();
        if (!normalizedEmail.contains("@")) {
            throw new IllegalArgumentException("Email must be valid");
        }
        return normalizedEmail;
    }

    private String normalizeMessage(String message) {
        String normalizedMessage = normalizeRequired(message, "Message is required");
        if (normalizedMessage.length() < 10) {
            throw new IllegalArgumentException("Message must have at least 10 characters");
        }
        return normalizedMessage;
    }
}
