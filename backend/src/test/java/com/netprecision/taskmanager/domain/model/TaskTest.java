package com.netprecision.taskmanager.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.netprecision.taskmanager.domain.service.TaskFactory;
import org.junit.jupiter.api.Test;

class TaskTest {

    private final TaskFactory taskFactory = new TaskFactory();

    @Test
    void shouldCreateTaskAsPendingByDefault() {
        Task task = taskFactory.create("Study DDD", "Read about tactical patterns");

        assertThat(task.id()).isNull();
        assertThat(task.title()).isEqualTo("Study DDD");
        assertThat(task.description()).isEqualTo("Read about tactical patterns");
        assertThat(task.completed()).isFalse();
    }

    @Test
    void shouldRejectTitleWithLessThanThreeCharacters() {
        assertThatThrownBy(() -> taskFactory.create("ab", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title must have at least 3 characters");
    }

    @Test
    void shouldTrimTitleAndEmptyDescription() {
        Task task = taskFactory.create("  New task  ", "   ");

        assertThat(task.title()).isEqualTo("New task");
        assertThat(task.description()).isNull();
    }

    @Test
    void shouldChangeStatus() {
        Task task = taskFactory.create("Create tests", null);

        task.changeStatus(true);
        assertThat(task.completed()).isTrue();

        task.changeStatus(false);
        assertThat(task.completed()).isFalse();
    }
}
