package app.domain.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import app.domain.models.enums.LoanStatus;
import app.domain.models.vo.Money;

@Setter
@Getter
@NoArgsConstructor
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
    
}