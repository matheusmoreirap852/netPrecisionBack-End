package com.netprecision.taskmanager.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.netprecision.taskmanager.application.dto.CreateTaskCommand;
import com.netprecision.taskmanager.application.dto.TaskResponse;
import com.netprecision.taskmanager.application.dto.UpdateTaskStatusCommand;
import com.netprecision.taskmanager.domain.model.Task;
import com.netprecision.taskmanager.domain.repository.TaskRepository;
import com.netprecision.taskmanager.domain.service.TaskFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskUseCaseTest {

    private InMemoryTaskRepository taskRepository;
    private TaskUseCase taskUseCase;

    @BeforeEach
    void setUp() {
        taskRepository = new InMemoryTaskRepository(new TaskFactory());
        taskUseCase = new TaskUseCase(taskRepository, new TaskFactory());
    }

    @Test
    void shouldCreateAndListTasks() {
        TaskResponse created = taskUseCase.createTask(new CreateTaskCommand("Write backend", "Spring Boot API"));

        assertThat(created.id()).isEqualTo(1L);
        assertThat(created.title()).isEqualTo("Write backend");
        assertThat(created.description()).isEqualTo("Spring Boot API");
        assertThat(created.completed()).isFalse();

        assertThat(taskUseCase.listTasks())
                .extracting(TaskResponse::title)
                .containsExactly("Write backend");
    }

    @Test
    void shouldUpdateTaskStatus() {
        TaskResponse created = taskUseCase.createTask(new CreateTaskCommand("Deploy app", null));

        TaskResponse updated = taskUseCase.updateStatus(created.id(), new UpdateTaskStatusCommand(true));

        assertThat(updated.completed()).isTrue();
    }

    @Test
    void shouldDeleteTask() {
        TaskResponse created = taskUseCase.createTask(new CreateTaskCommand("Remove item", null));

        taskUseCase.deleteTask(created.id());

        assertThat(taskUseCase.listTasks()).isEmpty();
    }

    @Test
    void shouldThrowWhenTaskDoesNotExist() {
        assertThatThrownBy(() -> taskUseCase.updateStatus(99L, new UpdateTaskStatusCommand(true)))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with id: 99");
    }

    private static class InMemoryTaskRepository implements TaskRepository {

        private final List<Task> tasks = new ArrayList<>();
        private final AtomicLong sequence = new AtomicLong();
        private final TaskFactory taskFactory;

        private InMemoryTaskRepository(TaskFactory taskFactory) {
            this.taskFactory = taskFactory;
        }

        @Override
        public List<Task> findAll() {
            return tasks.stream()
                    .sorted(Comparator.comparing(Task::id))
                    .map(this::copy)
                    .toList();
        }

        @Override
        public Optional<Task> findById(Long id) {
            return tasks.stream()
                    .filter(task -> task.id().equals(id))
                    .findFirst()
                    .map(this::copy);
        }

        @Override
        public Task save(Task task) {
            Task saved = task.id() == null
                    ? taskFactory.restore(sequence.incrementAndGet(), task.title(), task.description(), task.completed())
                    : copy(task);

            tasks.removeIf(existing -> existing.id().equals(saved.id()));
            tasks.add(saved);
            return copy(saved);
        }

        @Override
        public void delete(Task task) {
            tasks.removeIf(existing -> existing.id().equals(task.id()));
        }

        private Task copy(Task task) {
            return taskFactory.restore(task.id(), task.title(), task.description(), task.completed());
        }
    }
}
