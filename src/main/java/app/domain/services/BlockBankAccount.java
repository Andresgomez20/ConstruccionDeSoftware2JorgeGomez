package app.domain.services;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.enums.AccountStatus;
import app.domain.ports.BankAccountPort;

import java.util.HashMap;
import java.util.Map;

@Service
public class BlockBankAccount {
    
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;

    public BlockBankAccount(BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    public void execute(String accountNumber, Long analystId, String reason) throws BusinessException {
        // 1. Validaciones de entrada
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new BusinessException("El número de cuenta es requerido para realizar el bloqueo.");
        }
        if (analystId == null) {
            throw new BusinessException("El ID del analista es requerido para autorizar el bloqueo.");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException("El motivo del bloqueo es obligatorio.");
        }

        // 2. Búsqueda de la entidad a través del puerto
        BankAccount account = bankAccountPort.findByAccountNumber(accountNumber);
        
        if (account == null) {
            throw new BusinessException("La cuenta con el número especificado no fue encontrada.");
        }
        
        // 3. Reglas de Negocio: Transiciones de Estado
        if (account.getAccountStatus() == AccountStatus.BLOCKED) {
            throw new BusinessException("La cuenta ya se encuentra bloqueada.");
        }
        if (account.getAccountStatus() == AccountStatus.CANCELED) {
            throw new BusinessException("No se puede bloquear una cuenta que ya ha sido cancelada.");
        }

        // Guardamos el estado anterior para el registro detallado en la bitácora
        String estadoAnterior = account.getAccountStatus().name();
        
        // 4. Modificación de estado (Escenario exitoso)
        account.setAccountStatus(AccountStatus.BLOCKED);
        bankAccountPort.update(account);
        
        // 5. Registro en Bitácora
        Map<String, Object> details = new HashMap<>();
        details.put("motivoBloqueo", reason);
        details.put("estadoAnterior", estadoAnterior);
        details.put("nuevoEstado", AccountStatus.BLOCKED.name());
        
        logOperation.record(
            analystId, 
            "INTERNAL_ANALYST", 
            "BLOQUEO_CUENTA", 
            accountNumber, 
            details
        );
    }
}