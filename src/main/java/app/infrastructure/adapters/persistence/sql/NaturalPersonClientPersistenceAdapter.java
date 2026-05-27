package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.NaturalPersonClient;
import app.domain.ports.NaturalPersonClientPort;
import app.infrastructure.adapters.persistence.sql.entities.NaturalPersonClientEntity;
import app.infrastructure.adapters.persistence.sql.repositories.NaturalPersonClientRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NaturalPersonClientPersistenceAdapter implements NaturalPersonClientPort {

    private final NaturalPersonClientRepository repository;

    public NaturalPersonClientPersistenceAdapter(NaturalPersonClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(NaturalPersonClient client) {
        NaturalPersonClientEntity entityToSave = toEntity(client);
        
        if (entityToSave != null) {
            repository.save(entityToSave);
        }
    }

    // 1. Método requerido: Verificar si existe
    @Override
    public boolean existsByIdentificationNumber(String identificationNumber) {
        if (identificationNumber == null) {
            return false;
        }
        return repository.existsById(identificationNumber);
    }

    // 2. Método requerido: Buscar por Cédula
    @Override
    public NaturalPersonClient findByIdentificationNumber(String identificationNumber) {
        if (identificationNumber == null) {
            return null;
        }
        
        // Extraemos directamente
        NaturalPersonClientEntity entity = repository.findById(identificationNumber).orElse(null);
        
        if (entity != null) {
            return toDomain(entity);
        }
        
        return null;
    }

    // 3. Método requerido: Traer todos
    @Override
    public List<NaturalPersonClient> findAll() {
        List<NaturalPersonClientEntity> entities = repository.findAll();
        List<NaturalPersonClient> domainList = new ArrayList<>();
        
        for (NaturalPersonClientEntity entity : entities) {
            domainList.add(toDomain(entity));
        }
        return domainList;
    }

    // MAPPERS TRADICIONALES

    private NaturalPersonClientEntity toEntity(NaturalPersonClient domain) {
        if (domain == null) return null;
        
        NaturalPersonClientEntity entity = new NaturalPersonClientEntity();
        entity.setIdentificationNumber(domain.getIdentificationNumber());
        entity.setFullName(domain.getFullName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setBirthDate(domain.getBirthDate());
        entity.setAddress(domain.getAddress());
        
        return entity;
    }

    private NaturalPersonClient toDomain(NaturalPersonClientEntity entity) {
        if (entity == null) return null;
        
        NaturalPersonClient domain = new NaturalPersonClient();
        domain.setIdentificationNumber(entity.getIdentificationNumber());
        domain.setFullName(entity.getFullName());
        domain.setEmail(entity.getEmail());
        domain.setPhone(entity.getPhone());
        domain.setBirthDate(entity.getBirthDate());
        domain.setAddress(entity.getAddress());
        
        return domain;
    }
}