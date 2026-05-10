package app.infrastructure.adapters.persistence.sql.repositories;

import app.infrastructure.adapters.persistence.sql.entities.CompanyClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//  "String" en lugar de "Long" porque @Id es un texto (El NIT)
public interface CompanyClientRepository extends JpaRepository<CompanyClientEntity, String> {
    boolean existsByTaxIdentificationNumber(String nit);
}