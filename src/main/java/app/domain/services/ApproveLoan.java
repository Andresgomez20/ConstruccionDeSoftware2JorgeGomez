package app.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.entities.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.models.vo.Money;
import app.domain.ports.BankAccountPort;
import app.domain.ports.LoanPort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApproveLoan {

    private final LoanPort loanPort;
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;

    public ApproveLoan(LoanPort loanPort, BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.loanPort = loanPort;
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    @Transactional
    public void approveAndDisburse(Long loanId, boolean approve, String analystUserId) {
        Loan loan = loanPort.findById(loanId);
        if (loan == null) {
            throw new BusinessException("Préstamo no encontrado.");
        }

        if (loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new BusinessException("Solo los préstamos en estado 'EN_REVISIÓN' pueden ser aprobados o rechazados.");
        }

        // ESCENARIO: RECHAZO
        if (!approve) {
            loan.setStatus(LoanStatus.REJECTED);
            loanPort.update(loan);

            // Registro en Bitácora para rechazo
            Map<String, Object> details = new HashMap<>();
            details.put("reason", "Analyst decision");
            details.put("requestedAmount", loan.getRequestedAmount()); // Mongo guardará amount y currency
            
            logOperation.record(
                Long.parseLong(analystUserId), 
                "INTERNAL_ANALYST", 
                "LOAN_REJECTED", 
                loanId.toString(), 
                details
            );
            return;
        }

        // ESCENARIO: APROBACIÓN
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDate.now());
        loan.setApprovedAmount(loan.getRequestedAmount()); 

        if (loan.getDestinationAccount() == null || loan.getDestinationAccount().isEmpty()) {
            throw new BusinessException("Se requiere una cuenta de destino para realizar el desembolso.");
        }

        BankAccount destinationAccount = bankAccountPort.findByAccountNumber(loan.getDestinationAccount());
        if (destinationAccount == null || !destinationAccount.getAccountStatus().equals("ACTIVE")) {
            throw new BusinessException("La cuenta de destino no es válida o no se encuentra activa.");
        }
        
        // Extraemos el BigDecimal interno solo para la validación matemática de > 0
        if(loan.getApprovedAmount().getAmount().compareTo(BigDecimal.ZERO) <= 0){
             throw new BusinessException("El monto aprobado debe ser mayor que cero.");
        }

        // Aumentar saldo (Desembolso) usando el Value Object Money
        Money previousBalance = destinationAccount.getCurrentBalance();
        Money newBalance = previousBalance.add(loan.getApprovedAmount());
        destinationAccount.setCurrentBalance(newBalance);

        // Actualizar estados
        loan.setStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDate.now());

        // Guardar cambios en MySQL
        bankAccountPort.update(destinationAccount);
        loanPort.update(loan);

        // REGISTRO EN BITÁCORA PARA DESEMBOLSO EXITOSO
        Map<String, Object> details = new HashMap<>();
        details.put("disbursedAmount", loan.getApprovedAmount());
        details.put("destinationAccount", loan.getDestinationAccount());
        details.put("previousBalance", previousBalance);
        details.put("newBalance", newBalance);

        logOperation.record(
            Long.parseLong(analystUserId), 
            "INTERNAL_ANALYST", 
            "LOAN_DISBURSED", 
            loanId.toString(), 
            details
        );
    }
}