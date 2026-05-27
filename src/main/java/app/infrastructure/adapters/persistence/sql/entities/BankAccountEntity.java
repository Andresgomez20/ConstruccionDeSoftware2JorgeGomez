package app.infrastructure.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.Currency;

@Entity
@Table(name = "bank_accounts") // El nombre de la tabla 
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private String accountStatus;

    @Column(nullable = false)
    private Long titularId;

    @Column(nullable = false)
    private BigDecimal balanceAmount;

    @Column(nullable = false)
    private String balanceCurrency;

    @Column(nullable = false)
    private LocalDate openingDate;
    
    public BankAccountEntity() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    // ===============================================================
    // GETTERS Y SETTERS IMPERATIVOS (Conversión manual de String a Enum)
    // ===============================================================

    public AccountType getAccountType() { 
        if (this.accountType == null) return null;
        return AccountType.valueOf(this.accountType); 
    }
    
    public void setAccountType(AccountType accountType) { 
        if (accountType != null) {
            this.accountType = accountType.name(); 
        } else {
            this.accountType = null;
        }
    }
    
    public AccountStatus getAccountStatus() { 
        if (this.accountStatus == null) return null;
        return AccountStatus.valueOf(this.accountStatus); 
    }
    
    public void setAccountStatus(AccountStatus accountStatus) { 
        if (accountStatus != null) {
            this.accountStatus = accountStatus.name(); 
        } else {
            this.accountStatus = null;
        }
    }

    public Currency getBalanceCurrency() { 
        if (this.balanceCurrency == null) return null;
        return Currency.valueOf(this.balanceCurrency); 
    }
    
    public void setBalanceCurrency(Currency balanceCurrency) { 
        if (balanceCurrency != null) {
            this.balanceCurrency = balanceCurrency.name(); 
        } else {
            this.balanceCurrency = null;
        }
    }

    // ===============================================================
    
    public Long getTitularId() { return titularId; }
    public void setTitularId(Long titularId) { this.titularId = titularId; }
    
    public BigDecimal getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    
    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }
}