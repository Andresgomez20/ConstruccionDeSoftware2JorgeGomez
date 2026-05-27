package app.infrastructure.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class LoanEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientDocument;

    // Monto solicitado (Obligatorio)
    @Column(nullable = false)
    private BigDecimal requestedAmount;
    
    @Column(nullable = false)
    private String requestedCurrency;

    // Monto aprobado (Puede estar nulo al principio si el préstamo está "En estudio")
    @Column(nullable = true)
    private BigDecimal approvedAmount;
    
    @Column(nullable = true)
    private String approvedCurrency;

    @Column(nullable = false)
    private String destinationAccount;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate disbursementDate;
    
    public LoanEntity() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getClientDocument() { return clientDocument; }
    public void setClientDocument(String clientDocument) { this.clientDocument = clientDocument; }
    
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    
    public String getRequestedCurrency() { return requestedCurrency; }
    public void setRequestedCurrency(String requestedCurrency) { this.requestedCurrency = requestedCurrency; }
    
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    
    public String getApprovedCurrency() { return approvedCurrency; }
    public void setApprovedCurrency(String approvedCurrency) { this.approvedCurrency = approvedCurrency; }
    
    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate disbursementDate) { this.disbursementDate = disbursementDate; }
}