package com.netprecision.taskmanager.application.usecase;

import com.netprecision.taskmanager.application.dto.CreateTaskCommand;
import com.netprecision.taskmanager.application.dto.TaskResponse;
import com.netprecision.taskmanager.application.dto.UpdateTaskStatusCommand;
import com.netprecision.taskmanager.application.security.AuthenticatedUserProvider;
import com.netprecision.taskmanager.domain.model.Task;
import com.netprecision.taskmanager.domain.repository.TaskRepository;
import com.netprecision.taskmanager.domain.service.TaskFactory;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskUseCase implements BaseCrudUseCase<CreateTaskCommand, TaskResponse> {

    private final TaskRepository taskRepository;
    private final TaskFactory taskFactory;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public TaskUseCase(
            TaskRepository taskRepository,
            TaskFactory taskFactory,
            AuthenticatedUserProvider authenticatedUserProvider
    ) {
        this.taskRepository = taskRepository;
        this.taskFactory = taskFactory;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskResponse> findAll() {
        Long userId = authenticatedUserProvider.currentUserId();
        return taskRepository.findAllByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public TaskResponse findById(Long id) {
        Long userId = authenticatedUserProvider.currentUserId();
        return taskRepository.findByIdAndUserId(id, userId)
                .map(this::toResponse)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    @Override
    public TaskResponse create(CreateTaskCommand command) {
        Long userId = authenticatedUserProvider.currentUserId();
        Task task = taskFactory.create(userId, command.title(), command.description());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse updateStatus(Long id, UpdateTaskStatusCommand command) {
        Long userId = authenticatedUserProvider.currentUserId();
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.changeStatus(command.completed());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Long userId = authenticatedUserProvider.currentUserId();
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.id(), task.title(), task.description(), task.completed());
    }
}
