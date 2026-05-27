package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.identity.User;
import app.domain.ports.BankAccountPort;
import app.infrastructure.security.SecurityContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateBankAccount { 

    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;

    public CreateBankAccount(BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }
    
    public void execute(BankAccount bankAccount) throws BusinessException {
        // 1. Validaciones de entrada
        if (bankAccount == null) {
            throw new BusinessException("Los datos de la cuenta bancaria no pueden ser nulos.");
        }
        if (bankAccount.getAccountNumber() == null || bankAccount.getAccountNumber().trim().isEmpty()) {
            throw new BusinessException("El número de cuenta es obligatorio.");
        }
        if (bankAccount.getTitularId() == null || bankAccount.getTitularId().trim().isEmpty()) {
            throw new BusinessException("El ID del titular es obligatorio.");
        }
        if (bankAccount.getCurrentBalance() == null || bankAccount.getCurrentBalance().getAmount() == null) {
            throw new BusinessException("El saldo inicial de la cuenta debe estar definido.");
        }

        // 2. Obtener el usuario autenticado (Empleado) que está realizando la apertura
        User currentUser = SecurityContext.getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException("Usuario no autenticado. No se puede registrar la apertura.");
        }

        // 3. Reglas de Negocio
        if (bankAccount.getCurrentBalance().getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El saldo inicial de la cuenta no puede ser negativo.");
        }

        if (bankAccountPort.existsByAccountNumber(bankAccount.getAccountNumber())) {
            throw new BusinessException("Ya existe una cuenta bancaria registrada con este número.");
        }

        // 4. Asignación de valores por defecto
        if (bankAccount.getOpeningDate() == null) {
            bankAccount.setOpeningDate(LocalDate.now());
        }

        // 5. Persistencia Relacional
        bankAccountPort.save(bankAccount);

        // 6. Registro en Bitácora NoSQL 
        Map<String, Object> details = new HashMap<>();
        
        // Verificamos si accountType viene nulo
        if (bankAccount.getAccountType() != null) {
            details.put("tipoCuenta", bankAccount.getAccountType().name());
        }
        details.put("saldoInicial", bankAccount.getCurrentBalance().getAmount());
        details.put("idTitular", bankAccount.getTitularId());

        logOperation.record(
            currentUser.getId(), 
            currentUser.getRole().name(),
            "APERTURA_CUENTA", 
            bankAccount.getAccountNumber(), 
            details
        );
    }
}