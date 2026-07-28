package com.netprecision.taskmanager.infrastructure.persistence.repository;

import com.netprecision.taskmanager.infrastructure.persistence.entity.TaskJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, Long> {
}
