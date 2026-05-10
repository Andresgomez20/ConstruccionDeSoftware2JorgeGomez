package app.domain.services;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.ports.LoanPort;
import java.util.HashMap;
import java.util.Map;

@Service
public class RejectLoan {
    private final LoanPort loanPort;
    private final LogOperation logOperation;

    public RejectLoan(LoanPort loanPort, LogOperation logOperation) {
        this.loanPort = loanPort;
        this.logOperation = logOperation;
    }

    public void execute(Long loanId, String analystUserId, String reason) {
        Loan loan = loanPort.findById(loanId);
        if (loan == null) throw new BusinessException("Préstamo no encontrado.");
        if (loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new BusinessException("Solo los préstamos en estado 'EN_REVISIÓN' pueden ser rechazados.");
        }

        loan.setStatus(LoanStatus.REJECTED);
        loanPort.update(loan);

        Map<String, Object> details = new HashMap<>();
        details.put("reason", reason);
        details.put("requestedAmount", loan.getRequestedAmount());
        
        logOperation.record(Long.parseLong(analystUserId), "INTERNAL_ANALYST", "LOAN_REJECTED", loanId.toString(), details);
    }
}