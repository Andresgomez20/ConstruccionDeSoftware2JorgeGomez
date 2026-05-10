package app.infrastructure.adapters.persistence.sql.repositories;

import app.infrastructure.adapters.persistence.sql.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    
    // Spring Boot escribirá la consulta SQL por nosotros para buscar por documento
    List<LoanEntity> findByClientDocument(String clientDocument);
    
}