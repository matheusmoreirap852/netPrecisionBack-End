package com.netprecision.taskmanager.infrastructure.persistence.repository;

import com.netprecision.taskmanager.domain.model.User;
import com.netprecision.taskmanager.domain.repository.UserRepository;
import com.netprecision.taskmanager.infrastructure.persistence.mapper.UserPersistenceMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserPersistenceMapper mapper;

    public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository, UserPersistenceMapper mapper) {
        this.springDataUserRepository = springDataUserRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findById(Long id) {
        return springDataUserRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return mapper.toDomain(springDataUserRepository.save(mapper.toEntity(user)));
    }
}
