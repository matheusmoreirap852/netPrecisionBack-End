package com.netprecision.taskmanager.application.usecase;

import com.netprecision.taskmanager.application.dto.CreateTaskCommand;
import com.netprecision.taskmanager.application.dto.TaskResponse;
import com.netprecision.taskmanager.application.dto.UpdateTaskStatusCommand;
import com.netprecision.taskmanager.domain.model.Task;
import com.netprecision.taskmanager.domain.repository.TaskRepository;
import com.netprecision.taskmanager.domain.service.TaskFactory;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskFactory taskFactory;

    public TaskUseCase(TaskRepository taskRepository, TaskFactory taskFactory) {
        this.taskRepository = taskRepository;
        this.taskFactory = taskFactory;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> listTasks() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TaskResponse createTask(CreateTaskCommand command) {
        Task task = taskFactory.create(command.title(), command.description());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse updateStatus(Long id, UpdateTaskStatusCommand command) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.changeStatus(command.completed());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.id(), task.title(), task.description(), task.completed());
    }
}
