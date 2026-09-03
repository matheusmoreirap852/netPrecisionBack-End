package com.netprecision.taskmanager.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.netprecision.taskmanager.domain.service.TaskFactory;
import org.junit.jupiter.api.Test;

class TaskTest {

    private final TaskFactory taskFactory = new TaskFactory();

    @Test
    void shouldCreateTaskAsPendingByDefault() {
        Task task = taskFactory.create(1L, "Study DDD", "Read about tactical patterns");

        assertThat(task.id()).isNull();
        assertThat(task.userId()).isEqualTo(1L);
        assertThat(task.title()).isEqualTo("Study DDD");
        assertThat(task.description()).isEqualTo("Read about tactical patterns");
        assertThat(task.completed()).isFalse();
    }

    @Test
    void shouldRejectTitleWithLessThanThreeCharacters() {
        assertThatThrownBy(() -> taskFactory.create(1L, "ab", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title must have at least 3 characters");
    }

    @Test
    void shouldTrimTitleAndEmptyDescription() {
        Task task = taskFactory.create(1L, "  New task  ", "   ");

        assertThat(task.title()).isEqualTo("New task");
        assertThat(task.description()).isNull();
    }

    @Test
    void shouldChangeStatus() {
        Task task = taskFactory.create(1L, "Create tests", null);

        task.changeStatus(true);
        assertThat(task.completed()).isTrue();

        task.changeStatus(false);
        assertThat(task.completed()).isFalse();
    }
}
