package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.CompanyClient;
import app.domain.ports.CompanyClientPort;
import app.infrastructure.adapters.persistence.sql.entities.CompanyClientEntity;
import app.infrastructure.adapters.persistence.sql.repositories.CompanyClientRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyClientPersistenceAdapter implements CompanyClientPort {

    private final CompanyClientRepository repository;

    public CompanyClientPersistenceAdapter(CompanyClientRepository repository) {
        this.repository = repository;
    }

    // 1. MÉTODO SAVE
    @Override
    public void save(CompanyClient client) {
        CompanyClientEntity entityToSave = toEntity(client);
        
        if (entityToSave != null) {
            repository.save(entityToSave);
        }
    }

    // 2. MÉTODO REQUERIDO: Verificar si existe
    @Override
    public boolean existsByTaxIdentificationNumber(String nit) {
        if (nit == null) {
            return false;
        }
        return repository.existsById(nit); 
    }

    // 3. MÉTODO REQUERIDO: Buscar por NIT
    @Override
    public CompanyClient findByTaxIdentificationNumber(String nit) {
        // Validación imperativa defensiva
        if (nit == null) {
            return null;
        }
        
        // Ahora el compilador sabe que 'nit' es 100% seguro
        CompanyClientEntity entity = repository.findById(nit).orElse(null);
        
        if (entity != null) {
            return toDomain(entity);
        }
        
        return null;
    }

    // 4. MÉTODO REQUERIDO: Traer todos
    @Override
    public List<CompanyClient> findAll() {
        List<CompanyClientEntity> entities = repository.findAll();
        List<CompanyClient> domainList = new ArrayList<>();
        
        for (CompanyClientEntity entity : entities) {
            domainList.add(toDomain(entity));
        }
        return domainList;
    }

    // MAPPERS TRADICIONALES

    private CompanyClientEntity toEntity(CompanyClient domain) {
        if (domain == null) return null;
        
        CompanyClientEntity entity = new CompanyClientEntity();
        entity.setTaxIdentificationNumber(domain.getTaxIdentificationNumber());
        entity.setBusinessName(domain.getBusinessName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setAddress(domain.getAddress());
        entity.setLegalRepresentativeId(domain.getLegalRepresentativeId());
        
        return entity;
    }

    private CompanyClient toDomain(CompanyClientEntity entity) {
        if (entity == null) return null;
        
        CompanyClient domain = new CompanyClient();
        domain.setTaxIdentificationNumber(entity.getTaxIdentificationNumber());
        domain.setBusinessName(entity.getBusinessName());
        domain.setEmail(entity.getEmail());
        domain.setPhone(entity.getPhone());
        domain.setAddress(entity.getAddress());
        domain.setLegalRepresentativeId(entity.getLegalRepresentativeId());
        
        return domain;
    }
}