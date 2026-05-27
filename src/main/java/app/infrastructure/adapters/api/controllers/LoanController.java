package app.infrastructure.adapters.api.controllers;

import app.application.usecases.LoanUseCase;
import app.domain.models.entities.Loan;
import app.domain.Exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanUseCase loanUseCase;

    public LoanController(LoanUseCase loanUseCase) {
        this.loanUseCase = loanUseCase;
    }

    // 1. Endpoint para solicitar un préstamo (Asumiendo que agregarás requestLoan a tu UseCase)
    // Ejemplo en Postman: POST /api/loans
    @PostMapping
    public ResponseEntity<?> requestLoan(@RequestBody Loan loan) {
        try {
            loanUseCase.requestLoan(loan);
            return new ResponseEntity<>("Préstamo solicitado exitosamente", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para aprobar un préstamo
     * Requiere autenticación con rol INTERNAL_ANALYST
     * El usuario se obtiene automáticamente del JWT
     */
    // Ejemplo: PUT /api/loans/123/approve
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLoan(@PathVariable Long id) {
        try {
            loanUseCase.approveLoan(id);
            return ResponseEntity.ok("Préstamo aprobado exitosamente");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para RECHAZAR un préstamo
     * Requiere autenticación con rol INTERNAL_ANALYST
     * El usuario se obtiene automáticamente del JWT
     */
    // Ejemplo: PUT /api/loans/123/reject?reason=Ingresos%20insuficientes
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectLoan(@PathVariable Long id, 
                                        @RequestParam String reason) {
        try {
            loanUseCase.rejectLoan(id, reason);
            return ResponseEntity.ok("Préstamo rechazado. Motivo: " + reason);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para DESEMBOLSAR un préstamo
     * Requiere autenticación con rol INTERNAL_ANALYST
     * El usuario se obtiene automáticamente del JWT
     */
    // Ejemplo: PUT /api/loans/123/disburse
    @PutMapping("/{id}/disburse")
    public ResponseEntity<?> disburseLoan(@PathVariable Long id) {
        try {
            loanUseCase.disburseLoan(id);
            return ResponseEntity.ok("Préstamo desembolsado exitosamente en la cuenta destino");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}