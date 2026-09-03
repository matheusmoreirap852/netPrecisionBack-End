package com.netprecision.taskmanager.interfaces.rest;

import com.netprecision.taskmanager.application.dto.ContactMessageResponse;
import com.netprecision.taskmanager.application.dto.CreateContactMessageCommand;
import com.netprecision.taskmanager.application.usecase.ContactMessageUseCase;
import com.netprecision.taskmanager.interfaces.rest.dto.CreateContactMessageRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Mensagens publicas enviadas pelo portfolio")
public class ContactMessageController {

    private final ContactMessageUseCase contactMessageUseCase;

    public ContactMessageController(ContactMessageUseCase contactMessageUseCase) {
        this.contactMessageUseCase = contactMessageUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactMessageResponse create(@Valid @RequestBody CreateContactMessageRequest request) {
        return contactMessageUseCase.create(
                new CreateContactMessageCommand(
                        request.name(),
                        request.email(),
                        request.subject(),
                        request.message()
                )
        );
    }
}
