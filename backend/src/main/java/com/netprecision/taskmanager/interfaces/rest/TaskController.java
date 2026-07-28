package com.netprecision.taskmanager.interfaces.rest;

import com.netprecision.taskmanager.application.dto.CreateTaskCommand;
import com.netprecision.taskmanager.application.dto.TaskResponse;
import com.netprecision.taskmanager.application.dto.UpdateTaskStatusCommand;
import com.netprecision.taskmanager.application.usecase.TaskUseCase;
import com.netprecision.taskmanager.interfaces.rest.dto.CreateTaskRequest;
import com.netprecision.taskmanager.interfaces.rest.dto.UpdateTaskStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Task endpoints reuse the generic CRUD behavior from BaseController.
 * This class keeps only task-specific behavior, such as changing the completed status.
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@Tag(name = "Tasks", description = "Operacoes para gerenciar tarefas")
public class TaskController extends BaseController<CreateTaskRequest, CreateTaskCommand, TaskResponse> {

    private final TaskUseCase taskUseCase;

    public TaskController(TaskUseCase taskUseCase) {
        super(taskUseCase);
        this.taskUseCase = taskUseCase;
    }

    @Operation(summary = "Atualizar status da tarefa", description = "Marca uma tarefa como concluida ou pendente.")
    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        return taskUseCase.updateStatus(id, new UpdateTaskStatusCommand(request.completed()));
    }

    @Override
    protected CreateTaskCommand toCommand(CreateTaskRequest request) {
        return new CreateTaskCommand(request.title(), request.description());
    }

    @Override
    protected String resourcePath() {
        return "/api/tasks";
    }
}
