package com.netprecision.taskmanager.interfaces.rest;

import com.netprecision.taskmanager.application.dto.IdentifiableResponse;
import com.netprecision.taskmanager.application.usecase.BaseCrudUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Base REST controller for simple CRUD resources.
 * Concrete controllers keep only resource-specific mappings and request-to-command conversion.
 */
public abstract class BaseController<CreateRequest, CreateCommand, Response extends IdentifiableResponse> {

    private final BaseCrudUseCase<CreateCommand, Response> service;

    protected BaseController(BaseCrudUseCase<CreateCommand, Response> service) {
        this.service = service;
    }

    @Operation(summary = "Listar registros", description = "Retorna todos os registros do recurso.")
    @GetMapping
    public List<Response> getAll() {
        return service.findAll();
    }

    @Operation(summary = "Buscar registro por id", description = "Retorna um registro especifico pelo identificador.")
    @GetMapping("/{id}")
    public Response getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Criar registro", description = "Cria um novo registro para o recurso.")
    @PostMapping
    public ResponseEntity<Response> create(@Valid @RequestBody CreateRequest request) {
        Response response = service.create(toCommand(request));
        return ResponseEntity.created(URI.create(resourcePath() + "/" + response.id())).body(response);
    }

    @Operation(summary = "Excluir registro", description = "Remove um registro pelo identificador.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    protected abstract CreateCommand toCommand(CreateRequest request);

    protected abstract String resourcePath();
}
