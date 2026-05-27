package app.domain.models.entities;

import java.time.LocalDateTime;

import app.domain.models.enums.TransferStatus;
import app.domain.models.vo.Money;

public class Transfer {

    private Long id;
    private String originAccount;
    private String destinationAccount;
    private Money amount;
    private LocalDateTime creationDate;
    private LocalDateTime approvalDate;
    private TransferStatus status;
    private Long creatorUserId;
    private Long approverUserId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginAccount() { return originAccount; }
    public void setOriginAccount(String originAccount) { this.originAccount = originAccount; }

    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) { this.destinationAccount = destinationAccount; }

    public Money getAmount() { return amount; }
    public void setAmount(Money amount) { this.amount = amount; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate = approvalDate; }

    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }

    public Long getCreatorUserId() { return creatorUserId; }
    public void setCreatorUserId(Long creatorUserId) { this.creatorUserId = creatorUserId; }

    public Long getApproverUserId() { return approverUserId; }
    public void setApproverUserId(Long approverUserId) { this.approverUserId = approverUserId; }
}