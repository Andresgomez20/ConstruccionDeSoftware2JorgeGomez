package app.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.entities.Loan;
import app.domain.models.vo.Money;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.LoanStatus;
import app.domain.models.identity.User;
import app.domain.ports.BankAccountPort;
import app.domain.ports.LoanPort;
import app.infrastructure.security.SecurityContext;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DisburseLoan {
    private final LoanPort loanPort;
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;
    private final AuthorizationService authorizationService;

    public DisburseLoan(LoanPort loanPort, BankAccountPort bankAccountPort, LogOperation logOperation, AuthorizationService authorizationService) {
        this.loanPort = loanPort;
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
        this.authorizationService = authorizationService;
    }

    /**
     * Desembolsa un préstamo validando que el usuario sea INTERNAL_ANALYST
     * 
     * @param loanId ID del préstamo a desembolsar
     * @throws BusinessException si el préstamo no existe, no está aprobado, 
     *         la cuenta destino no es válida, o el usuario no tiene permisos
     */
    @Transactional
    public void execute(Long loanId) throws BusinessException {
        // Obtener el usuario autenticado del contexto de seguridad
        User currentUser = SecurityContext.getCurrentUser();
        
        // Validar que hay usuario autenticado
        if (currentUser == null) {
            throw new BusinessException("Usuario no autenticado.");
        }

        // Validar que el usuario tiene permisos de INTERNAL_ANALYST
        authorizationService.validateLoanApprovalPermission(currentUser);

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
        details.put("previousBalance", previousBalance);
        details.put("newBalance", newBalance);
        details.put("disbursingUsername", currentUser.getUsername());
        details.put("disbursingDocument", currentUser.getIdentificationId());
        
        logOperation.record(currentUser.getId(), currentUser.getRole().name(), "LOAN_DISBURSED", loanId.toString(), details);
    }
}