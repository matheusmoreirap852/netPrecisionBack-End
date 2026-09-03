package com.netprecision.taskmanager.domain.model;

import java.util.Objects;

public class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String passwordHash;

    public User(Long id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = normalizeName(name);
        this.email = normalizeEmail(email);
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password hash is required");
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

    public String passwordHash() {
        return passwordHash;
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        String normalizedName = name.trim();
        if (normalizedName.length() < 2) {
            throw new IllegalArgumentException("Name must have at least 2 characters");
        }
        return normalizedName;
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        String normalizedEmail = email.trim().toLowerCase();
        if (!normalizedEmail.contains("@")) {
            throw new IllegalArgumentException("Email must be valid");
        }
        return normalizedEmail;
    }
}
