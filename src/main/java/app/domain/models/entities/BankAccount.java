package app.domain.models.entities;

import java.time.LocalDate;

import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.vo.Money;

public class BankAccount {

    private String accountNumber;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private String titularId;
    private Money currentBalance;
    private LocalDate openingDate;

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public String getTitularId() {
        return titularId;
    }

    public Money getCurrentBalance() {
        return currentBalance;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    // Setters
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setTitularId(String titularId) {
        this.titularId = titularId;
    }

    public void setCurrentBalance(Money currentBalance) {
        this.currentBalance = currentBalance;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
}