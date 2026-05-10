package app.infrastructure.adapters.persistence.sql;

import app.domain.models.identity.User;
import app.domain.ports.UserPort;
import app.infrastructure.adapters.persistence.sql.entities.UserEntity;
import app.infrastructure.adapters.persistence.sql.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPersistenceAdapter implements UserPort {

    private final UserRepository repository;

    public UserPersistenceAdapter(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByIdentificationId(String identificationId) {
        return repository.existsByIdentificationId(identificationId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public void save(User user) {
        repository.save(toEntity(user));
    }

    @Override
    public void update(User user) {
        // En JPA save hace update si el ID existe
        repository.save(toEntity(user));
    }

    @Override
    public User findByIdentificationId(String identificationId) {
        return repository.findByIdentificationId(identificationId)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username)
                .map(this::toDomain)
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // --- MAPPERS ---

    private UserEntity toEntity(User domain) {
        if (domain == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setRelatedEntityId(domain.getRelatedEntityId());
        entity.setFullName(domain.getFullName());
        entity.setIdentificationId(domain.getIdentificationId());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setBirthDate(domain.getBirthDate());
        entity.setAddress(domain.getAddress());
        entity.setUsername(domain.getUsername());
        entity.setPassword(domain.getPassword());
        entity.setRole(domain.getRole());
        entity.setStatus(domain.getStatus());
        return entity;
    }

    private User toDomain(UserEntity entity) {
        if (entity == null) return null;
        User domain = new User();
        domain.setId(entity.getId());
        domain.setRelatedEntityId(entity.getRelatedEntityId());
        domain.setFullName(entity.getFullName());
        domain.setIdentificationId(entity.getIdentificationId());
        domain.setEmail(entity.getEmail());
        domain.setPhone(entity.getPhone());
        domain.setBirthDate(entity.getBirthDate());
        domain.setAddress(entity.getAddress());
        domain.setUsername(entity.getUsername());
        domain.setPassword(entity.getPassword());
        domain.setRole(entity.getRole());
        domain.setStatus(entity.getStatus());
        return domain;
    }
}