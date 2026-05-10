package app.infrastructure.adapters.persistence.sql.repositories;

import app.infrastructure.adapters.persistence.sql.entities.NaturalPersonClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaturalPersonClientRepository extends JpaRepository<NaturalPersonClientEntity, String> {
    boolean existsByIdentificationNumber(String id);
}