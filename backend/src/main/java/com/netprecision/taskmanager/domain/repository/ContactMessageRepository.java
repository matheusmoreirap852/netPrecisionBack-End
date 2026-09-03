package com.netprecision.taskmanager.domain.repository;

import com.netprecision.taskmanager.domain.model.ContactMessage;

public interface ContactMessageRepository {

    ContactMessage save(ContactMessage contactMessage);
}
