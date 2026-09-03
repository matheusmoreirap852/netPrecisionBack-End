package com.netprecision.taskmanager.infrastructure.persistence.repository;

import com.netprecision.taskmanager.domain.model.ContactMessage;
import com.netprecision.taskmanager.domain.repository.ContactMessageRepository;
import com.netprecision.taskmanager.infrastructure.persistence.mapper.ContactMessagePersistenceMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JpaContactMessageRepositoryAdapter implements ContactMessageRepository {

    private final SpringDataContactMessageRepository springDataContactMessageRepository;
    private final ContactMessagePersistenceMapper mapper;

    public JpaContactMessageRepositoryAdapter(
            SpringDataContactMessageRepository springDataContactMessageRepository,
            ContactMessagePersistenceMapper mapper
    ) {
        this.springDataContactMessageRepository = springDataContactMessageRepository;
        this.mapper = mapper;
    }

    @Override
    public ContactMessage save(ContactMessage contactMessage) {
        return mapper.toDomain(springDataContactMessageRepository.save(mapper.toEntity(contactMessage)));
    }
}
