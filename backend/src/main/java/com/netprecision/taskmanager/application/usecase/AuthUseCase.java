package com.netprecision.taskmanager.application.usecase;

import com.netprecision.taskmanager.application.dto.AuthResponse;
import com.netprecision.taskmanager.application.dto.AuthUserResponse;
import com.netprecision.taskmanager.application.dto.LoginCommand;
import com.netprecision.taskmanager.application.dto.RegisterCommand;
import com.netprecision.taskmanager.application.security.AuthenticationException;
import com.netprecision.taskmanager.domain.model.User;
import com.netprecision.taskmanager.domain.repository.UserRepository;
import com.netprecision.taskmanager.infrastructure.security.JwtTokenService;
import com.netprecision.taskmanager.infrastructure.security.PasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthUseCase {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtTokenService jwtTokenService;

    public AuthUseCase(
            UserRepository userRepository,
            PasswordService passwordService,
            JwtTokenService jwtTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtTokenService = jwtTokenService;
    }

    @Transactional
    public AuthResponse register(RegisterCommand command) {
        String email = command.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User(null, command.name(), email, passwordService.hash(command.password()));
        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginCommand command) {
        User user = userRepository.findByEmail(command.email().trim().toLowerCase())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        if (!passwordService.matches(command.password(), user.passwordHash())) {
            throw new AuthenticationException("Invalid email or password");
        }

        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        return new AuthResponse(
                jwtTokenService.generate(user.id()),
                new AuthUserResponse(user.id(), user.name(), user.email())
        );
    }
}
