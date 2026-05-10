package app.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.entities.Loan;
import app.domain.models.vo.Money;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.LoanStatus;
import app.domain.ports.BankAccountPort;
import app.domain.ports.LoanPort;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DisburseLoan {
    private final LoanPort loanPort;
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;

    public DisburseLoan(LoanPort loanPort, BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.loanPort = loanPort;
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    @Transactional
    public void execute(Long loanId, String analystUserId) {
        Loan loan = loanPort.findById(loanId);
        if (loan == null) throw new BusinessException("Préstamo no encontrado.");
        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new BusinessException("El préstamo debe estar APROBADO para ser desembolsado.");
        }

        BankAccount destinationAccount = bankAccountPort.findByAccountNumber(loan.getDestinationAccount());
        if (destinationAccount == null || destinationAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("La cuenta de destino no es válida o no se encuentra activa.");
        }

        Money previousBalance = destinationAccount.getCurrentBalance();
        Money newBalance = previousBalance.add(loan.getApprovedAmount());
        destinationAccount.setCurrentBalance(newBalance);

        loan.setStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDate.now());

        bankAccountPort.update(destinationAccount);
        loanPort.update(loan);

        Map<String, Object> details = new HashMap<>();
        details.put("disbursedAmount", loan.getApprovedAmount());
        details.put("destinationAccount", loan.getDestinationAccount());
        logOperation.record(Long.parseLong(analystUserId), "SYSTEM", "LOAN_DISBURSED", loanId.toString(), details);
    }
}