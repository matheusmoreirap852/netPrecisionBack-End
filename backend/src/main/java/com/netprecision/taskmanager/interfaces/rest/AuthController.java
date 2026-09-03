package com.netprecision.taskmanager.interfaces.rest;

import com.netprecision.taskmanager.application.dto.AuthResponse;
import com.netprecision.taskmanager.application.dto.LoginCommand;
import com.netprecision.taskmanager.application.dto.RegisterCommand;
import com.netprecision.taskmanager.application.usecase.AuthUseCase;
import com.netprecision.taskmanager.interfaces.rest.dto.LoginRequest;
import com.netprecision.taskmanager.interfaces.rest.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Cadastro e autenticacao de usuarios")
public class AuthController {

    private final AuthUseCase authUseCase;

    public AuthController(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authUseCase.register(new RegisterCommand(request.name(), request.email(), request.password()));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authUseCase.login(new LoginCommand(request.email(), request.password()));
    }
}
