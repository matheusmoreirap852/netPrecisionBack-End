package com.netprecision.taskmanager.application.security;

@FunctionalInterface
public interface AuthenticatedUserProvider {

    Long currentUserId();
}
