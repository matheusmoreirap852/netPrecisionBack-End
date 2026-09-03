package com.netprecision.taskmanager.application.usecase;

import com.netprecision.taskmanager.application.dto.ContactMessageResponse;
import com.netprecision.taskmanager.application.dto.CreateContactMessageCommand;
import com.netprecision.taskmanager.domain.model.ContactMessage;
import com.netprecision.taskmanager.domain.repository.ContactMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactMessageUseCase {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageUseCase(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }

    @Transactional
    public ContactMessageResponse create(CreateContactMessageCommand command) {
        ContactMessage contactMessage = new ContactMessage(
                null,
                command.name(),
                command.email(),
                command.subject(),
                command.message(),
                null
        );
        return toResponse(contactMessageRepository.save(contactMessage));
    }

    private ContactMessageResponse toResponse(ContactMessage contactMessage) {
        return new ContactMessageResponse(
                contactMessage.id(),
                contactMessage.name(),
                contactMessage.email(),
                contactMessage.subject(),
                contactMessage.message(),
                contactMessage.createdAt()
        );
    }
}
