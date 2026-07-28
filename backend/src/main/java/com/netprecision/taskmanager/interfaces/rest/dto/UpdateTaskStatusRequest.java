package com.netprecision.taskmanager.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(@NotNull(message = "Completed status is required") Boolean completed) {
}
