package com.netprecision.taskmanager.infrastructure.persistence.repository;

import com.netprecision.taskmanager.domain.model.Task;
import com.netprecision.taskmanager.domain.repository.TaskRepository;
import com.netprecision.taskmanager.infrastructure.persistence.mapper.TaskPersistenceMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaTaskRepositoryAdapter implements TaskRepository {

    private final SpringDataTaskRepository springDataTaskRepository;
    private final TaskPersistenceMapper mapper;

    public JpaTaskRepositoryAdapter(SpringDataTaskRepository springDataTaskRepository, TaskPersistenceMapper mapper) {
        this.springDataTaskRepository = springDataTaskRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        return springDataTaskRepository.findAllByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Task> findByIdAndUserId(Long id, Long userId) {
        return springDataTaskRepository.findByIdAndUserId(id, userId)
                .map(mapper::toDomain);
    }

    @Override
    public Task save(Task task) {
        return mapper.toDomain(springDataTaskRepository.save(mapper.toEntity(task)));
    }

    @Override
    public void delete(Task task) {
        springDataTaskRepository.deleteById(task.id());
    }
}
