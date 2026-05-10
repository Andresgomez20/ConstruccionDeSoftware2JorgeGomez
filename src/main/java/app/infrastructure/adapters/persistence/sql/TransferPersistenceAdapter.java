package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.Transfer;
import app.domain.models.enums.TransferStatus;
import app.domain.models.vo.Money;
import app.domain.ports.TransferPort;
import app.infrastructure.adapters.persistence.sql.entities.TransferEntity;
import app.infrastructure.adapters.persistence.sql.repositories.TransferRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TransferPersistenceAdapter implements TransferPort {

    private final TransferRepository repository;

    public TransferPersistenceAdapter(TransferRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Transfer transfer) {
        repository.save(toEntity(transfer));
    }

    @Override
    public Transfer findById(Long id) {
        Optional<TransferEntity> optional = repository.findById(id);
        if (optional.isPresent()) {
            return toDomain(optional.get());
        }
        return null;
    }

    @Override
    public void update(Transfer transfer) {
        Optional<TransferEntity> optional = repository.findById(transfer.getId());
        if (optional.isPresent()) {
            TransferEntity entity = toEntity(transfer);
            entity.setId(optional.get().getId());
            repository.save(entity);
        }
    }

    @Override
    public List<Transfer> findByStatus(TransferStatus status) {
        List<TransferEntity> entities = repository.findByStatus(status);
        List<Transfer> domainList = new ArrayList<>();
        for (TransferEntity entity : entities) {
            domainList.add(toDomain(entity));
        }
        return domainList;
    }

    @Override
    public List<Transfer> findByOriginAccount(String account) {
        List<TransferEntity> entities = repository.findBySourceAccountNumber(account);
        List<Transfer> domainList = new ArrayList<>();
        for (TransferEntity entity : entities) {
            domainList.add(toDomain(entity));
        }
        return domainList;
    }

    // MAPPERS

    private TransferEntity toEntity(Transfer domain) {
        if (domain == null) return null;
        TransferEntity entity = new TransferEntity();
        
        // Usamos los nombres: Origin, Destination, CreationDate
        entity.setSourceAccountNumber(domain.getOriginAccount());
        entity.setDestinationAccountNumber(domain.getDestinationAccount());
        entity.setTimestamp(domain.getCreationDate());
        
        if (domain.getAmount() != null) {
            entity.setAmount(domain.getAmount().getAmount());
            entity.setCurrency(domain.getAmount().getCurrency());
        }
        
        entity.setStatus(domain.getStatus());
        return entity;
    }

    private Transfer toDomain(TransferEntity entity) {
        if (entity == null) return null;
        Transfer domain = new Transfer();
        
        domain.setId(entity.getId());
        
        // Usamos los nombres: Origin, Destination, CreationDate
        domain.setOriginAccount(entity.getSourceAccountNumber());
        domain.setDestinationAccount(entity.getDestinationAccountNumber());
        domain.setCreationDate(entity.getTimestamp());
        
        if (entity.getAmount() != null && entity.getCurrency() != null) {
            domain.setAmount(new Money(entity.getAmount(), entity.getCurrency()));
        }
        
        domain.setStatus(entity.getStatus());
        return domain;
    }
}