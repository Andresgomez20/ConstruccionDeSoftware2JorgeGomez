package app.infrastructure.adapters.persistence.sql.repositories;

import app.infrastructure.adapters.persistence.sql.entities.TransferEntity;
import app.domain.models.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferRepository extends JpaRepository<TransferEntity, Long> {
    
    List<TransferEntity> findBySourceAccountNumber(String sourceAccountNumber);
    
    List<TransferEntity> findByStatus(TransferStatus status);
}