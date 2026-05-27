package app.infrastructure.adapters.persistence.sql.entities;

import app.domain.models.enums.TransferStatus;
import app.domain.models.enums.Currency;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceAccountNumber;
    
    @Column(nullable = false)
    private String destinationAccountNumber;

    // Descomponemos el VO Money
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    public TransferEntity() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    // GETTERS Y SETTERS 

    public Currency getCurrency() { 
        if (this.currency == null) return null;
        return Currency.valueOf(this.currency); 
    }
    
    public void setCurrency(Currency currency) { 
        if (currency != null) {
            this.currency = currency.name(); 
        } else {
            this.currency = null;
        }
    }
    
    public TransferStatus getStatus() { 
        if (this.status == null) return null;
        return TransferStatus.valueOf(this.status); 
    }
    
    public void setStatus(TransferStatus status) { 
        if (status != null) {
            this.status = status.name(); 
        } else {
            this.status = null;
        }
    }
    // ===============================================================

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}