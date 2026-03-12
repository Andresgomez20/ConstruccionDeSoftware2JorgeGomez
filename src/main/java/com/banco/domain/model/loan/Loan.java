package com.banco.domain.model.loan;

import java.util.UUID;
import com.banco.domain.model.vo.Money;
import com.banco.domain.model.account.Account;
import com.banco.domain.exceptions.DomainException;


 // Representa un Préstamo en el sistema.

public class Loan {
    private final UUID loanId;
    private final UUID customerId;
    private final Money amount;
    private LoanStatus status;

    public Loan(UUID loanId, UUID customerId, Money amount) {
        if (amount.amount().doubleValue() <= 0) {
            throw new DomainException("El monto del préstamo debe ser mayor a cero.");
        }
        this.loanId = loanId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = LoanStatus.EN_ESTUDIO;
    }

    
     //Regla de Negocio: Solo el Analista Interno puede aprobar.

    public void approve(UserRole userRole) {
        if (userRole != UserRole.ANALISTA_INTERNO) {
            throw new DomainException("Acceso denegado: Solo el Analista Interno puede aprobar préstamos.");
        }
        
        if (this.status != LoanStatus.EN_ESTUDIO) {
            throw new DomainException("El préstamo debe estar 'En Estudio' para ser aprobado.");
        }

        this.status = LoanStatus.APROBADO;
    }

    /**
     * Desembolsa el dinero en la cuenta del cliente.
     * @param targetAccount Cuenta donde se depositará el préstamo.
     */
    public void disburse(Account targetAccount) {
        if (this.status != LoanStatus.APROBADO) {
            throw new DomainException("No se puede desembolsar un préstamo que no esté aprobado.");
        }

        // Realiza el depósito en la cuenta vinculada
        targetAccount.deposit(this.amount);
        this.status = LoanStatus.DESEMBOLSADO;
    }

    // Getters
    public UUID getLoanId() { return loanId; }
    public UUID getCustomerId() { return customerId; }
    public Money getAmount() { return amount; }
    public LoanStatus getStatus() { return status; }
}