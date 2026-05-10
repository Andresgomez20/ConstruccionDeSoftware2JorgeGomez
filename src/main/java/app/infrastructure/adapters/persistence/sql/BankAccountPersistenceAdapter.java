package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.BankAccount;
import app.domain.models.vo.Money;
import app.domain.ports.BankAccountPort;
import app.infrastructure.adapters.persistence.sql.entities.BankAccountEntity;
import app.infrastructure.adapters.persistence.sql.repositories.BankAccountRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BankAccountPersistenceAdapter implements BankAccountPort {

    private final BankAccountRepository repository;

    public BankAccountPersistenceAdapter(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        // Estilo imperativo: Abriendo la caja del Optional con un 'if'
        Optional<BankAccountEntity> entityOptional = repository.findByAccountNumber(accountNumber);
        
        if (entityOptional.isPresent()) {
            return toDomain(entityOptional.get());
        }
        
        return null;
    }

    @Override
    public void update(BankAccount account) {
        // Estilo imperativo: Sin usar lambdas (.ifPresent)
        Optional<BankAccountEntity> entityOptional = repository.findByAccountNumber(account.getAccountNumber());
        
        if (entityOptional.isPresent()) {
            BankAccountEntity existing = entityOptional.get();
            BankAccountEntity entityToUpdate = toEntity(account);
            
            // Le pasamos el ID de la base de datos para que JPA sepa que debe actualizar, no crear uno nuevo
            entityToUpdate.setId(existing.getId()); 
            
            repository.save(entityToUpdate);
        }
    }

    @Override
    public void save(BankAccount account) {
        repository.save(toEntity(account));
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<BankAccount> findByTitularId(String titularId) {
        Long ownerId = Long.parseLong(titularId);
        
        // 1. Buscamos en base de datos
        List<BankAccountEntity> entities = repository.findByTitularId(ownerId);
        
        // 2. Creamos lista vacía (Estilo clásico, nada de streams)
        List<BankAccount> domainAccounts = new ArrayList<>();
        
        // 3. Recorremos con un for-each tradicional
        for (BankAccountEntity entity : entities) {
            BankAccount domain = toDomain(entity);
            domainAccounts.add(domain);
        }
        
        return domainAccounts;
    }

    // MAPPERS (Conversión de capas)

    private BankAccount toDomain(BankAccountEntity entity) {
        BankAccount domain = new BankAccount();
        domain.setAccountNumber(entity.getAccountNumber());
        domain.setCurrentBalance(new Money(entity.getBalanceAmount(), entity.getBalanceCurrency()));
        domain.setAccountType(entity.getAccountType());
        domain.setAccountStatus(entity.getAccountStatus());
        
        // ¡No olvidemos los campos que faltaban!
        domain.setTitularId(String.valueOf(entity.getTitularId()));
        domain.setOpeningDate(entity.getOpeningDate());
        
        return domain;
    }

    private BankAccountEntity toEntity(BankAccount account) {
        BankAccountEntity entity = new BankAccountEntity();
        entity.setAccountNumber(account.getAccountNumber());
        entity.setBalanceAmount(account.getCurrentBalance().getAmount());
        entity.setBalanceCurrency(account.getCurrentBalance().getCurrency());
        entity.setAccountType(account.getAccountType());
        entity.setAccountStatus(account.getAccountStatus());
        
        if (account.getTitularId() != null) {
            entity.setTitularId(Long.valueOf(account.getTitularId()));
        }
        entity.setOpeningDate(account.getOpeningDate());
        
        return entity;
    }
}