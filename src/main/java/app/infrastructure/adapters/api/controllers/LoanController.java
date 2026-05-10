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
    @PostMapping
    public ResponseEntity<?> requestLoan(@RequestBody Loan loan) {
        try {
            // Asume que tienes este método en tu LoanUseCase
            // loanUseCase.requestLoan(loan);
            return new ResponseEntity<>("Préstamo solicitado exitosamente", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. Endpoint para aprobar un préstamo
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLoan(@PathVariable Long id, @RequestParam String analystId) {
        try {
            // Asume que tienes este método en tu LoanUseCase
            // loanUseCase.approveLoan(id, analystId);
            return ResponseEntity.ok("Préstamo aprobado exitosamente");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 3. Endpoint para RECHAZAR un préstamo (El servicio que hicimos hoy)
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectLoan(@PathVariable Long id, 
                                        @RequestParam String analystId, 
                                        @RequestParam String reason) {
        try {
            loanUseCase.rejectLoan(id, analystId, reason);
            return ResponseEntity.ok("Préstamo rechazado. Motivo: " + reason);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 4. Endpoint para DESEMBOLSAR un préstamo (El otro servicio que hicimos hoy)
    @PutMapping("/{id}/disburse")
    public ResponseEntity<?> disburseLoan(@PathVariable Long id, @RequestParam String analystId) {
        try {
            loanUseCase.disburseLoan(id, analystId);
            return ResponseEntity.ok("Préstamo desembolsado exitosamente en la cuenta destino");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}