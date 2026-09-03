package com.netprecision.taskmanager.infrastructure.persistence.mapper;

import com.netprecision.taskmanager.domain.model.User;
import com.netprecision.taskmanager.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toDomain(UserJpaEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPasswordHash());
    }

    public UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(user.id(), user.name(), user.email(), user.passwordHash());
    }
}
