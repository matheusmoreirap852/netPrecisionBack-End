package com.netprecision.taskmanager.infrastructure.persistence.mapper;

import com.netprecision.taskmanager.domain.model.Task;
import com.netprecision.taskmanager.domain.service.TaskFactory;
import com.netprecision.taskmanager.infrastructure.persistence.entity.TaskJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskPersistenceMapper {

    private final TaskFactory taskFactory;

    public TaskPersistenceMapper(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    public Task toDomain(TaskJpaEntity entity) {
        return taskFactory.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.isCompleted()
        );
    }

    public TaskJpaEntity toEntity(Task task) {
        return new TaskJpaEntity(task.id(), task.userId(), task.title(), task.description(), task.completed());
    }
}
