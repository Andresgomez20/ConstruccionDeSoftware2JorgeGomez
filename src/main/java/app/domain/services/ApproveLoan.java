package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.models.identity.User;
import app.domain.ports.LoanPort;
import app.infrastructure.security.SecurityContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApproveLoan {

    private final LoanPort loanPort;
    private final LogOperation logOperation;
    private final AuthorizationService authorizationService;

    public ApproveLoan(LoanPort loanPort, LogOperation logOperation, AuthorizationService authorizationService) {
        this.loanPort = loanPort;
        this.logOperation = logOperation;
        this.authorizationService = authorizationService;
    }

    //Aprueba un préstamo validando que el usuario autenticado sea INTERNAL_ANALYST
     
    public void execute(Long loanId) throws BusinessException {
        // Obtener el usuario autenticado del contexto de seguridad
        User currentUser = SecurityContext.getCurrentUser();
        
        // Validar que hay usuario autenticado
        if (currentUser == null) {
            throw new BusinessException("Usuario no autenticado.");
        }

        // Validar que el usuario tiene permisos de INTERNAL_ANALYST
        authorizationService.validateLoanApprovalPermission(currentUser);

        // Obtener el préstamo
        Loan loan = loanPort.findById(loanId);
        
        if (loan == null) {
            throw new BusinessException("Préstamo no encontrado.");
        }

        // REGLA DE NEGOCIO: Transiciones de Estado
        if (loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new BusinessException("Solo los préstamos en estado 'EN_REVISIÓN' pueden ser aprobados.");
        }

        // Asignamos el monto solicitado como monto aprobado. 
        loan.setApprovedAmount(loan.getRequestedAmount()); 

        // REGLA DE NEGOCIO: Impacto Financiero y Validaciones
        // Extraemos el monto interno del Value Object Money
        if (loan.getApprovedAmount() == null || loan.getApprovedAmount().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto aprobado debe ser estrictamente mayor a cero.");
        }

        // Guardamos el estado anterior para la bitácora
        String estadoAnterior = loan.getStatus().name();

        // ESCENARIO: APROBACIÓN
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDate.now());

        // Guardar cambios a través del puerto (SQL)
        loanPort.update(loan);

        // REGISTRO EN BITÁCORA PARA APROBACIÓN
        Map<String, Object> details = new HashMap<>();
        // Guardamos el número puro extraído del Money
        details.put("montoAprobado", loan.getApprovedAmount().getAmount());
        details.put("tasaInteres", loan.getInterestRate()); 
        details.put("estadoAnterior", estadoAnterior);
        details.put("nuevoEstado", loan.getStatus().name());
        details.put("idAnalistaAprobador", currentUser.getId());

        logOperation.record(
            currentUser.getId(), 
            currentUser.getRole().name(), 
            "APROBACION_PRESTAMO", 
            loanId.toString(), 
            details
        );
    }
}