package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.ports.LoanPort;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApproveLoan {

    private final LoanPort loanPort;
    private final LogOperation logOperation;

    public ApproveLoan(LoanPort loanPort, LogOperation logOperation) {
        this.loanPort = loanPort;
        this.logOperation = logOperation;
    }

    public void execute(Long loanId, String analystUserId) {
        Loan loan = loanPort.findById(loanId);
        
        if (loan == null) {
            throw new BusinessException("Préstamo no encontrado.");
        }

        if (loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new BusinessException("Solo los préstamos en estado 'EN_REVISIÓN' pueden ser aprobados.");
        }

        // ESCENARIO: APROBACIÓN
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDate.now());
        
        // Asignamos el monto solicitado como monto aprobado. 
        loan.setApprovedAmount(loan.getRequestedAmount()); 

        // Guardar cambios a través del puerto
        loanPort.update(loan);

        // REGISTRO EN BITÁCORA PARA APROBACIÓN
        Map<String, Object> details = new HashMap<>();
        details.put("approvedAmount", loan.getApprovedAmount());
        details.put("destinationAccount", loan.getDestinationAccount());

        logOperation.record(
            Long.parseLong(analystUserId), 
            "INTERNAL_ANALYST", 
            "LOAN_APPROVED", 
            loanId.toString(), 
            details
        );
    }
}