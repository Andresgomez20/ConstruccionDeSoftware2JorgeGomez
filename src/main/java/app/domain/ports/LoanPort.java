package app.domain.ports;

import java.util.List;

import app.domain.models.entities.Loan;

public interface LoanPort {

    void save(Loan loan);
    void update(Loan loan);
    Loan findById(Long id);
    List<Loan> findByClientDocument(String clientDocument);
    
}