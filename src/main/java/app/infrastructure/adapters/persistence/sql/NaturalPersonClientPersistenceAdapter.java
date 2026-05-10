package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.NaturalPersonClient;
import app.domain.ports.NaturalPersonClientPort;
import app.infrastructure.adapters.persistence.sql.entities.NaturalPersonClientEntity;
import app.infrastructure.adapters.persistence.sql.repositories.NaturalPersonClientRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class NaturalPersonClientPersistenceAdapter implements NaturalPersonClientPort {

    private final NaturalPersonClientRepository repository;

    public NaturalPersonClientPersistenceAdapter(NaturalPersonClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(NaturalPersonClient client) {
        repository.save(toEntity(client));
    }

    // 1. Método requerido: Verificar si existe
    @Override
    public boolean existsByIdentificationNumber(String identificationNumber) {
        // Al igual que con el NIT, la cédula es nuestra llave primaria (@Id), así que usamos existsById
        return repository.existsById(identificationNumber);
    }

    // 2. Método requerido: Buscar por Cédula en lugar del "findById" genérico
    @Override
    public NaturalPersonClient findByIdentificationNumber(String identificationNumber) {
        Optional<NaturalPersonClientEntity> optional = repository.findById(identificationNumber);
        if (optional.isPresent()) {
            return toDomain(optional.get());
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