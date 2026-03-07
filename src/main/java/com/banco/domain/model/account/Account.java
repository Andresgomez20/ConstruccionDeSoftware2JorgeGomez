package com.banco.domain.model.account;

import java.util.UUID;
import com.banco.domain.model.vo.Money;
import com.banco.domain.exceptions.DomainException;

public class Account {
    private final String accountNumber;
    private final UUID ownerId;
    private Money balance;
    private AccountStatus status;

    public Account(String accountNumber, UUID ownerId, Money initialBalance) {
        this.accountNumber = accountNumber;
        this.ownerId = ownerId;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVA;
    }

    // Comportamiento: Depositar dinero
    public void deposit(Money amount) {
        ensureActive();
        this.balance = this.balance.add(amount);
    }

    // Comportamiento: Retirar dinero
    public void withdraw(Money amount) {
        ensureActive();
        
        // Regla de Negocio: No permitir saldo negativo
        if (this.balance.amount().compareTo(amount.amount()) < 0) {
            throw new DomainException("Saldo insuficiente para realizar el retiro");
        }
        
        // Creamos un nuevo objeto Money con la resta (inmutabilidad)
        java.math.BigDecimal newAmount = this.balance.amount().subtract(amount.amount());
        this.balance = new Money(newAmount, this.balance.currency());
    }

    private void ensureActive() {
        if (this.status != AccountStatus.ACTIVA) {
            throw new DomainException("La cuenta no está activa para realizar operaciones");
        }
    }

    // Getters de solo lectura
    public String getAccountNumber() { return accountNumber; }
    public UUID getOwnerId() { return ownerId; }
    public Money getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
}