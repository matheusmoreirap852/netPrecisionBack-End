package com.netprecision.taskmanager.infrastructure.persistence.repository;

import com.netprecision.taskmanager.infrastructure.persistence.entity.ContactMessageJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataContactMessageRepository extends JpaRepository<ContactMessageJpaEntity, Long> {
}
