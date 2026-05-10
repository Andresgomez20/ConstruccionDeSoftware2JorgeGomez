package app.infrastructure.adapters.persistence.sql.repositories;

import app.infrastructure.adapters.persistence.sql.entities.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    
    // 1. Perfecto para buscar una cuenta específica
    Optional<BankAccountEntity> findByAccountNumber(String accountNumber);
    
    // 2. Perfecto para tu validación de "ya existe"
    boolean existsByAccountNumber(String accountNumber);
    
    // 3. ¡CORREGIDO! Ahora coincide con el nombre de tu variable en la Entidad (titularId)
    List<BankAccountEntity> findByTitularId(Long titularId); 
}