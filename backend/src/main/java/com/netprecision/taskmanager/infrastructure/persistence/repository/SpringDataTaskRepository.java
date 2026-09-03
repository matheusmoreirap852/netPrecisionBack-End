package com.netprecision.taskmanager.infrastructure.persistence.repository;

import com.netprecision.taskmanager.infrastructure.persistence.entity.TaskJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, Long> {

    List<TaskJpaEntity> findAllByUserId(Long userId);

    Optional<TaskJpaEntity> findByIdAndUserId(Long id, Long userId);
}
