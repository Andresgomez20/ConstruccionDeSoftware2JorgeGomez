package app.domain.ports;

import java.util.List;

import app.domain.models.entities.BankAccount;

public interface BankAccountPort {

    boolean existsByAccountNumber(String accountNumber);
    void save(BankAccount bankAccount);
    void update(BankAccount bankAccount);
    BankAccount findByAccountNumber(String accountNumber);
    List<BankAccount> findByTitularId(String titularId);
    
}