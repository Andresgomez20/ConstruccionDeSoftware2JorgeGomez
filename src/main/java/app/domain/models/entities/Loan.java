package app.domain.models.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import app.domain.models.enums.LoanStatus;
import app.domain.models.vo.Money;

public class Loan {

    private Long id;
    private String loanType;
    private String clientDocument; // ID del solicitante
    private Money requestedAmount;
    private Money approvedAmount;
    private BigDecimal interestRate;
    private Integer termMonths;
    private LoanStatus status;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private String destinationAccount; // Cuenta donde se abona
    
    public Loan() {}
    
    public Loan(Long id, String loanType, String clientDocument, Money requestedAmount, Money approvedAmount, BigDecimal interestRate, Integer termMonths, LoanStatus status, LocalDate approvalDate, LocalDate disbursementDate, String destinationAccount) {
        this.id = id;
        this.loanType = loanType;
        this.clientDocument = clientDocument;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.status = status;
        this.approvalDate = approvalDate;
        this.disbursementDate = disbursementDate;
        this.destinationAccount = destinationAccount;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLoanType() {
        return loanType;
    }
    
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
    
    public String getClientDocument() {
        return clientDocument;
    }
    
    public void setClientDocument(String clientDocument) {
        this.clientDocument = clientDocument;
    }
    
    public Money getRequestedAmount() {
        return requestedAmount;
    }
    
    public void setRequestedAmount(Money requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
    
    public Money getApprovedAmount() {
        return approvedAmount;
    }
    
    public void setApprovedAmount(Money approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public Integer getTermMonths() {
        return termMonths;
    }
    
    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }
    
    public LoanStatus getStatus() {
        return status;
    }
    
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
    
    public LocalDate getApprovalDate() {
        return approvalDate;
    }
    
    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }
    
    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }
    
    public String getDestinationAccount() {
        return destinationAccount;
    }
    
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    
}