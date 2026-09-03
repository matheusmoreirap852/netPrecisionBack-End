package com.netprecision.taskmanager.infrastructure.security;

import com.netprecision.taskmanager.application.security.AuthenticatedUserProvider;
import com.netprecision.taskmanager.application.security.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserContext implements AuthenticatedUserProvider {

    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        CURRENT_USER_ID.set(userId);
    }

    public static void clear() {
        CURRENT_USER_ID.remove();
    }

    @Override
    public Long currentUserId() {
        Long userId = CURRENT_USER_ID.get();
        if (userId == null) {
            throw new AuthenticationException("Authentication is required");
        }
        return userId;
    }
}
