package com.netprecision.taskmanager.application.dto;

public record AuthResponse(String token, AuthUserResponse user) {
}
