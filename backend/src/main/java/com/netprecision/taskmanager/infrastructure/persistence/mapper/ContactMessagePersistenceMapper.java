package com.netprecision.taskmanager.infrastructure.persistence.mapper;

import com.netprecision.taskmanager.domain.model.ContactMessage;
import com.netprecision.taskmanager.infrastructure.persistence.entity.ContactMessageJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ContactMessagePersistenceMapper {

    public ContactMessage toDomain(ContactMessageJpaEntity entity) {
        return new ContactMessage(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getSubject(),
                entity.getMessage(),
                entity.getCreatedAt()
        );
    }

    public ContactMessageJpaEntity toEntity(ContactMessage contactMessage) {
        return new ContactMessageJpaEntity(
                contactMessage.id(),
                contactMessage.name(),
                contactMessage.email(),
                contactMessage.subject(),
                contactMessage.message(),
                contactMessage.createdAt()
        );
    }
}
