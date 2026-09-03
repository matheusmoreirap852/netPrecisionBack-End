package com.netprecision.taskmanager.application.dto;

public record CreateContactMessageCommand(String name, String email, String subject, String message) {
}
