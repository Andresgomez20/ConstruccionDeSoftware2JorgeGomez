package app.infrastructure.adapters.persistence.sql.repositories;

import app.infrastructure.adapters.persistence.sql.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByIdentificationId(String identificationId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByIdentificationId(String identificationId);
    Optional<UserEntity> findByUsername(String username);
}