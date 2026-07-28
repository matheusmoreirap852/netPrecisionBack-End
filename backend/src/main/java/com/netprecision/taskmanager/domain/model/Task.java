package com.netprecision.taskmanager.domain.model;

import java.util.Objects;

public class Task {

    private final Long id;
    private final TaskTitle title;
    private final String description;
    private boolean completed;

    public Task(Long id, TaskTitle title, String description, boolean completed) {
        this.id = id;
        this.title = Objects.requireNonNull(title, "Title is required");
        this.description = normalizeDescription(description);
        this.completed = completed;
    }

    public Long id() {
        return id;
    }

    public String title() {
        return title.value();
    }

    public String description() {
        return description;
    }

    public boolean completed() {
        return completed;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    public void markAsPending() {
        this.completed = false;
    }

    public void changeStatus(boolean completed) {
        if (completed) {
            markAsCompleted();
            return;
        }
        markAsPending();
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description.trim();
    }
}
