package com.netprecision.taskmanager.domain.repository;

import com.netprecision.taskmanager.domain.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findAll();

    Optional<Task> findById(Long id);

    Task save(Task task);

    void delete(Task task);
}
