package com.netprecision.taskmanager.application.dto;

public record TaskResponse(Long id, String title, String description, boolean completed) {
}
