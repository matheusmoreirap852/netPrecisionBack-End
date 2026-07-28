package com.netprecision.taskmanager.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 120, message = "Title must have between 3 and 120 characters")
        String title,

        @Size(max = 500, message = "Description must have at most 500 characters")
        String description
) {
}
