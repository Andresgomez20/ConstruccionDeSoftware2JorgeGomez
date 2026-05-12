package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.models.vo.Money;
import app.domain.ports.LoanPort;
import app.infrastructure.adapters.persistence.sql.entities.LoanEntity;
import app.infrastructure.adapters.persistence.sql.repositories.LoanRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import  app.domain.models.enums.Currency;
import java.util.List;
import java.util.Optional;

@Component
public class LoanPersistenceAdapter implements LoanPort {

    private final LoanRepository repository;

    public LoanPersistenceAdapter(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Loan loan) {
        repository.save(toEntity(loan));
    }

    @Override
    public Loan findById(Long id) {
    
        Optional<LoanEntity> entityOptional = repository.findById(id);
        
        if (entityOptional.isPresent()) {
            return toDomain(entityOptional.get());
        }
        
        return null; // Si no existe, devolvemos null 
    }

    @Override
    public void update(Loan loan) {

        Optional<LoanEntity> entityOptional = repository.findById(loan.getId());
        
        if (entityOptional.isPresent()) {
            LoanEntity existing = entityOptional.get();
            LoanEntity entityToUpdate = toEntity(loan);
            entityToUpdate.setId(existing.getId()); 
            
            repository.save(entityToUpdate);
        }
    }

    @Override
    public List<Loan> findByClientDocument(String document) {
        // Buscamos en la base de datos
        List<LoanEntity> entities = repository.findByClientDocument(document);
        
        // Creamos la lista vacía 
        List<Loan> domainLoans = new ArrayList<>();
        // Convertimos cada entidad a dominio y la agregamos a la lista
        for (LoanEntity entity : entities) {
            domainLoans.add(toDomain(entity));
        }
        
        return domainLoans;
    }

    // MAPPERS


    private LoanEntity toEntity(Loan loan) {
        if (loan == null) return null;
        
        LoanEntity entity = new LoanEntity();
        entity.setId(loan.getId());
        entity.setClientDocument(loan.getClientDocument());
        entity.setDestinationAccount(loan.getDestinationAccount());
        entity.setStatus(loan.getStatus().toString());
        entity.setDisbursementDate(loan.getDisbursementDate());

        // Desempaquetando el VO Money (Requested)
        if (loan.getRequestedAmount() != null) {
            entity.setRequestedAmount(loan.getRequestedAmount().getAmount());
            entity.setRequestedCurrency(loan.getRequestedAmount().getCurrency().toString());
        }

        // Desempaquetando el VO Money (Approved)
        if (loan.getApprovedAmount() != null) {
            entity.setApprovedAmount(loan.getApprovedAmount().getAmount());
            entity.setApprovedCurrency(loan.getApprovedAmount().getCurrency().toString());
        }

        return entity;
    }

    private Loan toDomain(LoanEntity entity) {
        if (entity == null) return null;
        
        Loan loan = new Loan();
        loan.setId(entity.getId());
        loan.setClientDocument(entity.getClientDocument());
        loan.setDestinationAccount(entity.getDestinationAccount());
        loan.setStatus(LoanStatus.valueOf(entity.getStatus()));
        loan.setDisbursementDate(entity.getDisbursementDate());

        // Empaquetando el VO Money (Requested)
        if (entity.getRequestedAmount() != null && entity.getRequestedCurrency() != null) {
            loan.setRequestedAmount(new Money(entity.getRequestedAmount(), Currency.valueOf(entity.getRequestedCurrency())));
        }

        // Empaquetando el VO Money (Approved)
        if (entity.getApprovedAmount() != null && entity.getApprovedCurrency() != null) {
            loan.setApprovedAmount(new Money(entity.getApprovedAmount(), Currency.valueOf(entity.getApprovedCurrency())));
        }

        return loan;
    }
}