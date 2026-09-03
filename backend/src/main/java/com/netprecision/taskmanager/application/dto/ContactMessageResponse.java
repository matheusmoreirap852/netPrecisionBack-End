package com.netprecision.taskmanager.application.dto;

import java.time.OffsetDateTime;

public record ContactMessageResponse(
        Long id,
        String name,
        String email,
        String subject,
        String message,
        OffsetDateTime createdAt
) {
}
