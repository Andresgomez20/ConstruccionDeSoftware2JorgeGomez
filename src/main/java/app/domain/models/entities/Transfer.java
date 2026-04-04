package app.domain.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import app.domain.models.enums.TransferStatus;
import app.domain.models.vo.Money;

@Setter
@Getter
@NoArgsConstructor
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

}