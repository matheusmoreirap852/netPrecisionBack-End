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
        taskUseCase = new TaskUseCase(taskRepository, new TaskFactory(), () -> 1L);
    }

    @Test
    void shouldCreateAndListTasks() {
        TaskResponse created = taskUseCase.create(new CreateTaskCommand("Write backend", "Spring Boot API"));

        assertThat(created.id()).isEqualTo(1L);
        assertThat(created.title()).isEqualTo("Write backend");
        assertThat(created.description()).isEqualTo("Spring Boot API");
        assertThat(created.completed()).isFalse();

        assertThat(taskUseCase.findAll())
                .extracting(TaskResponse::title)
                .containsExactly("Write backend");
    }

    @Test
    void shouldUpdateTaskStatus() {
        TaskResponse created = taskUseCase.create(new CreateTaskCommand("Deploy app", null));

        TaskResponse updated = taskUseCase.updateStatus(created.id(), new UpdateTaskStatusCommand(true));

        assertThat(updated.completed()).isTrue();
    }

    @Test
    void shouldDeleteTask() {
        TaskResponse created = taskUseCase.create(new CreateTaskCommand("Remove item", null));

        taskUseCase.delete(created.id());

        assertThat(taskUseCase.findAll()).isEmpty();
    }

    @Test
    void shouldFindTaskById() {
        TaskResponse created = taskUseCase.create(new CreateTaskCommand("Find item", null));

        TaskResponse found = taskUseCase.findById(created.id());

        assertThat(found.title()).isEqualTo("Find item");
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
        public List<Task> findAllByUserId(Long userId) {
            return tasks.stream()
                    .filter(task -> task.userId().equals(userId))
                    .sorted(Comparator.comparing(Task::id))
                    .map(this::copy)
                    .toList();
        }

        @Override
        public Optional<Task> findByIdAndUserId(Long id, Long userId) {
            return tasks.stream()
                    .filter(task -> task.id().equals(id))
                    .filter(task -> task.userId().equals(userId))
                    .findFirst()
                    .map(this::copy);
        }

        @Override
        public Task save(Task task) {
            Task saved = task.id() == null
                    ? taskFactory.restore(sequence.incrementAndGet(), task.userId(), task.title(), task.description(), task.completed())
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
            return taskFactory.restore(task.id(), task.userId(), task.title(), task.description(), task.completed());
        }
    }
}
