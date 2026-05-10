package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.CompanyClient;
import app.domain.ports.CompanyClientPort;
import app.infrastructure.adapters.persistence.sql.entities.CompanyClientEntity;
import app.infrastructure.adapters.persistence.sql.repositories.CompanyClientRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CompanyClientPersistenceAdapter implements CompanyClientPort {

    private final CompanyClientRepository repository;

    public CompanyClientPersistenceAdapter(CompanyClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(CompanyClient client) {
        repository.save(toEntity(client));
    }

    // 1. Método requerido: Verificar si existe
    @Override
    public boolean existsByTaxIdentificationNumber(String nit) {
        // En Spring Data, como el NIT es el @Id, podemos usar existsById directamente
        return repository.existsById(nit); 
    }

    // 2. Método requerido: Buscar por NIT en lugar del "findById" genérico
    @Override
    public CompanyClient findByTaxIdentificationNumber(String nit) {
        Optional<CompanyClientEntity> optional = repository.findById(nit);
        if (optional.isPresent()) {
            return toDomain(optional.get());
        }
        return null;
    }

    // 3. Método requerido: Traer todos
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