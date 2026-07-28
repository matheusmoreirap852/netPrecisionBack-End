package com.netprecision.taskmanager.domain.model;

import java.util.Objects;

public record TaskTitle(String value) {

    private static final int MIN_LENGTH = 3;

    public TaskTitle {
        value = Objects.requireNonNull(value, "Title is required").trim();
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Title must have at least 3 characters");
        }
    }
}
