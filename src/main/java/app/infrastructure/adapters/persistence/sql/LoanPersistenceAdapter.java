package app.infrastructure.adapters.persistence.sql;

import app.domain.models.entities.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.Currency;
import app.domain.models.vo.Money;
import app.domain.ports.LoanPort;
import app.infrastructure.adapters.persistence.sql.entities.LoanEntity;
import app.infrastructure.adapters.persistence.sql.repositories.LoanRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoanPersistenceAdapter implements LoanPort {

    private final LoanRepository repository;

    public LoanPersistenceAdapter(LoanRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Loan loan) {
        LoanEntity entityToSave = toEntity(loan);
        
        if (entityToSave != null) {
            repository.save(entityToSave);
        }
    }

    @Override
    public Loan findById(Long id) {
        if (id == null) {
            return null;
        }
        LoanEntity entity = repository.findById(id).orElse(null);
        
        if (entity != null) {
            return toDomain(entity);
        }
        
        return null; 
    }

    @Override
    public void update(Loan loan) {
        // 1. Validación de seguridad inicial
        if (loan == null) {
            return;
        }

        // 2. Extraemos el ID
        Long loanId = loan.getId();
        
        // 3. Validamos la variable local
        if (loanId == null) {
            return;
        }

        // 4. Extracción directa del préstamo existente para asegurar que el ID es correcto
        LoanEntity existing = repository.findById(loanId).orElse(null);
        
        if (existing != null) {
            LoanEntity entityToUpdate = toEntity(loan);
            
            if (entityToUpdate != null) {
                entityToUpdate.setId(existing.getId()); 
                repository.save(entityToUpdate);
            }
        }
    }

    @Override
    public List<Loan> findByClientDocument(String document) {
        List<Loan> domainLoans = new ArrayList<>();
        
        if (document == null) {
            return domainLoans;
        }
        
        List<LoanEntity> entities = repository.findByClientDocument(document);
        
        for (LoanEntity entity : entities) {
            domainLoans.add(toDomain(entity));
        }
        
        return domainLoans;
    }

    // =========================================================
    // MAPPERS (Conversiones seguras entre Dominio y Persistencia)
    // =========================================================

    private LoanEntity toEntity(Loan loan) {
        if (loan == null) return null;
        
        LoanEntity entity = new LoanEntity();
        entity.setId(loan.getId());
        entity.setClientDocument(loan.getClientDocument());
        entity.setDestinationAccount(loan.getDestinationAccount());
        
        if (loan.getStatus() != null) {
            entity.setStatus(loan.getStatus().toString());
        }
        
        entity.setDisbursementDate(loan.getDisbursementDate());

        // Desempaquetando el VO Money (Requested) de forma defensiva
        if (loan.getRequestedAmount() != null) {
            entity.setRequestedAmount(loan.getRequestedAmount().getAmount());
            if (loan.getRequestedAmount().getCurrency() != null) {
                entity.setRequestedCurrency(loan.getRequestedAmount().getCurrency().toString());
            }
        }

        // Desempaquetando el VO Money (Approved) de forma defensiva
        if (loan.getApprovedAmount() != null) {
            entity.setApprovedAmount(loan.getApprovedAmount().getAmount());
            if (loan.getApprovedAmount().getCurrency() != null) {
                entity.setApprovedCurrency(loan.getApprovedAmount().getCurrency().toString());
            }
        }

        return entity;
    }

    private Loan toDomain(LoanEntity entity) {
        if (entity == null) return null;
        
        Loan loan = new Loan();
        loan.setId(entity.getId());
        loan.setClientDocument(entity.getClientDocument());
        loan.setDestinationAccount(entity.getDestinationAccount());
        
        if (entity.getStatus() != null) {
            loan.setStatus(LoanStatus.valueOf(entity.getStatus()));
        }
        
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