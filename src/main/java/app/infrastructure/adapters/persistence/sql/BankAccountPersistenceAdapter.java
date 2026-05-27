package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.BankAccount;
import app.domain.models.vo.Money;
import app.domain.ports.BankAccountPort;
import app.infrastructure.adapters.persistence.sql.entities.BankAccountEntity;
import app.infrastructure.adapters.persistence.sql.repositories.BankAccountRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BankAccountPersistenceAdapter implements BankAccountPort {

    private final BankAccountRepository repository;

    public BankAccountPersistenceAdapter(BankAccountRepository repository) {
        this.repository = repository;
    }

    // 1. ÚNICO MÉTODO FIND
    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        BankAccountEntity entity = repository.findByAccountNumber(accountNumber).orElse(null);
        
        if (entity != null) {
            return toDomain(entity);
        }
        
        return null;
    }

    // 2. MÉTODO SAVE 
    @Override
    public void save(BankAccount account) {
        BankAccountEntity entityToSave = toEntity(account);
        
        if (entityToSave != null) {
            repository.save(entityToSave);
        }
    }

    // 3. MÉTODO UPDATE 
    @Override
    public void update(BankAccount account) {
        BankAccountEntity existing = repository.findByAccountNumber(account.getAccountNumber()).orElse(null);
        
        if (existing != null) {
            BankAccountEntity entityToUpdate = toEntity(account);
            entityToUpdate.setId(existing.getId()); 
            repository.save(entityToUpdate);
        }
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<BankAccount> findByTitularId(String titularId) {
        Long ownerId = Long.parseLong(titularId);
        
        List<BankAccountEntity> entities = repository.findByTitularId(ownerId);
        List<BankAccount> domainAccounts = new ArrayList<>();
        
        for (BankAccountEntity entity : entities) {
            BankAccount domain = toDomain(entity);
            domainAccounts.add(domain);
        }
        
        return domainAccounts;
    }

    // MAPPERS (Conversión limpia entre la Capa de Dominio y de Persistencia)

    private BankAccount toDomain(BankAccountEntity entity) {
        if (entity == null) return null;
        
        BankAccount domain = new BankAccount();
        domain.setAccountNumber(entity.getAccountNumber());
        domain.setCurrentBalance(new Money(entity.getBalanceAmount(), entity.getBalanceCurrency()));
        domain.setAccountType(entity.getAccountType());
        domain.setAccountStatus(entity.getAccountStatus());
        domain.setTitularId(String.valueOf(entity.getTitularId()));
        domain.setOpeningDate(entity.getOpeningDate());
        
        return domain;
    }

    private BankAccountEntity toEntity(BankAccount account) {
        if (account == null) return null;
        
        BankAccountEntity entity = new BankAccountEntity();
        entity.setAccountNumber(account.getAccountNumber());
        
        if (account.getCurrentBalance() != null) {
            entity.setBalanceAmount(account.getCurrentBalance().getAmount());
            entity.setBalanceCurrency(account.getCurrentBalance().getCurrency());
        }
        
        entity.setAccountType(account.getAccountType());
        entity.setAccountStatus(account.getAccountStatus());
        
        if (account.getTitularId() != null) {
            entity.setTitularId(Long.valueOf(account.getTitularId()));
        }
        entity.setOpeningDate(account.getOpeningDate());
        
        return entity;
    }
}