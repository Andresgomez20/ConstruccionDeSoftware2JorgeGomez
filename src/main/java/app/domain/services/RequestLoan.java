package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.Loan;
import app.domain.models.enums.LoanStatus;
import app.domain.ports.LoanPort;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class RequestLoan {

    private final LoanPort loanPort;
    private final LogOperation logOperation; // 1. Inyectamos la bitácora

    public RequestLoan(LoanPort loanPort, LogOperation logOperation) {
        this.loanPort = loanPort;
        this.logOperation = logOperation;
    }

    public void request(Loan loan) {
        // CORRECCIÓN: Usamos .getAmount() para extraer el número y compararlo con el ZERO
        if (loan.getRequestedAmount() == null || loan.getRequestedAmount().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto solicitado debe ser mayor que cero.");
        }
        
        // Regla de Negocio: El estado inicial siempre es "En estudio" (UNDER_REVIEW)
        loan.setStatus(LoanStatus.UNDER_REVIEW);
        
        // Guardar en MySQL
        loanPort.save(loan);

        // REGISTRO EN BITÁCORA
        Map<String, Object> details = new HashMap<>();
        // Mongo guardará el objeto Money (Ej: { "amount": 500000, "currency": "COP" })
        details.put("montoSolicitado", loan.getRequestedAmount());
        details.put("tipoPrestamo", loan.getLoanType());
        details.put("plazoMeses", loan.getTermMonths());
        details.put("cuentaDestino", loan.getDestinationAccount());
        details.put("documentoCliente", loan.getClientDocument());

        logOperation.record(
            0L, 
            "CLIENT_REQUEST", 
            "LOAN_APPLICATION_SUBMITTED", 
            loan.getClientDocument(),
            details
        );
    }
}