package com.netprecision.taskmanager.domain.repository;

import com.netprecision.taskmanager.domain.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAllByUserId(Long userId);

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    Task save(Task task);

    void delete(Task task);
}
