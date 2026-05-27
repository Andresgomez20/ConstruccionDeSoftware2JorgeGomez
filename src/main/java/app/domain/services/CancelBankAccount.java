package app.domain.services;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.enums.AccountStatus;
import app.domain.ports.BankAccountPort;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CancelBankAccount {
    
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;

    public CancelBankAccount(BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    public void execute(String accountNumber, Long clientId) throws BusinessException {
        // 1. Validaciones de entrada
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new BusinessException("El número de cuenta es requerido para realizar la cancelación.");
        }
        if (clientId == null) {
            throw new BusinessException("El ID del cliente/usuario es requerido para autorizar la cancelación.");
        }

        // 2. Búsqueda de la entidad a través del puerto
        BankAccount account = bankAccountPort.findByAccountNumber(accountNumber);
        
        if (account == null) {
            throw new BusinessException("La cuenta con el número especificado no fue encontrada.");
        }

        // 3. Reglas de Negocio: Transiciones de Estado
        if (account.getAccountStatus() == AccountStatus.CANCELED) {
            throw new BusinessException("La cuenta ya se encuentra cancelada.");
        }

        // 4. Regla de Negocio: Validar saldo cero
        if (account.getCurrentBalance() == null || account.getCurrentBalance().getAmount() == null) {
            throw new BusinessException("Error de integridad: El saldo de la cuenta no está definido.");
        }
        
        if (account.getCurrentBalance().getAmount().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("El saldo debe ser exactamente cero para poder cancelar la cuenta.");
        }

        // Guardamos el estado anterior para la bitácora
        String estadoAnterior = account.getAccountStatus().name();

        // 5. Modificación de estado (Escenario exitoso)
        account.setAccountStatus(AccountStatus.CANCELED);
        bankAccountPort.update(account);

        // 6. Registro en Bitácora NoSQL
        Map<String, Object> details = new HashMap<>();
        details.put("saldoFinal", account.getCurrentBalance().getAmount());
        details.put("estadoAnterior", estadoAnterior);
        details.put("nuevoEstado", AccountStatus.CANCELED.name());

        logOperation.record(
            clientId, 
            "CLIENT",
            "CANCELACION_CUENTA", 
            accountNumber, 
            details
        );
    }
}