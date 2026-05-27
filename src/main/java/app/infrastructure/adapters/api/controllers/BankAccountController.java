package app.infrastructure.adapters.api.controllers;

import app.application.usecases.BankAccountUseCase;
import app.domain.models.entities.BankAccount;
import app.domain.Exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

    private final BankAccountUseCase bankAccountUseCase;

    public BankAccountController(BankAccountUseCase bankAccountUseCase) {
        this.bankAccountUseCase = bankAccountUseCase;
    }

    // 1. Endpoint para Crear una Cuenta Bancaria
    // Ejemplo en Postman: POST /api/bank-accounts
    @PostMapping
    public ResponseEntity<?> create(@RequestBody BankAccount bankAccount) {
        try {
            bankAccountUseCase.createBankAccount(bankAccount);
            return new ResponseEntity<>("Cuenta bancaria creada exitosamente", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. Endpoint para Bloquear una Cuenta Bancaria
    // Ejemplo: PUT /api/bank-accounts/123456789/block?analystId=1&reason=Movimientos%20sospechosos
    @PutMapping("/{accountNumber}/block")
    public ResponseEntity<?> block(@PathVariable String accountNumber, 
                                   @RequestParam Long analystId, 
                                   @RequestParam String reason) {
        try {
            bankAccountUseCase.blockBankAccount(accountNumber, analystId, reason);
            return ResponseEntity.ok("Cuenta bloqueada exitosamente. Motivo: " + reason);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 3. Endpoint para Cancelar una Cuenta Bancaria
    // Ejemplo: PUT /api/bank-accounts/123456789/cancel?clientId=99
    @PutMapping("/{accountNumber}/cancel")
    public ResponseEntity<?> cancel(@PathVariable String accountNumber, 
                                    @RequestParam Long clientId) {
        try {
            bankAccountUseCase.cancelBankAccount(accountNumber, clientId);
            return ResponseEntity.ok("Cuenta cancelada exitosamente.");
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 4. Endpoint para Buscar una Cuenta Bancaria
    // Ejemplo: GET /api/bank-accounts/123456789
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        try {
            BankAccount account = bankAccountUseCase.findByAccountNumber(accountNumber);
            if (account == null) {
                return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}