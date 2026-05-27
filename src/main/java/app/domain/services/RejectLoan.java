package app.domain.services;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.models.identity.User;
import app.domain.ports.LoanPort;
import app.infrastructure.security.SecurityContext;
import java.util.HashMap;
import java.util.Map;

@Service
public class RejectLoan {
    private final LoanPort loanPort;
    private final LogOperation logOperation;
    private final AuthorizationService authorizationService;

    public RejectLoan(LoanPort loanPort, LogOperation logOperation, AuthorizationService authorizationService) {
        this.loanPort = loanPort;
        this.logOperation = logOperation;
        this.authorizationService = authorizationService;
    }

    /**
     * Rechaza un préstamo validando que el usuario sea INTERNAL_ANALYST
     * 
     * @param loanId ID del préstamo a rechazar
     * @param reason Motivo del rechazo
     * @throws BusinessException si el préstamo no existe, no está en estado UNDER_REVIEW, 
     *         o el usuario no tiene permisos
     */
    public void execute(Long loanId, String reason) throws BusinessException {
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
        if (loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new BusinessException("Solo los préstamos en estado 'EN_REVISIÓN' pueden ser rechazados.");
        }

        loan.setStatus(LoanStatus.REJECTED);
        loanPort.update(loan);

        Map<String, Object> details = new HashMap<>();
        details.put("reason", reason);
        details.put("requestedAmount", loan.getRequestedAmount());
        details.put("rejectorUsername", currentUser.getUsername());
        details.put("rejectorDocument", currentUser.getIdentificationId());
        
        logOperation.record(currentUser.getId(), currentUser.getRole().name(), "LOAN_REJECTED", loanId.toString(), details);
    }
}