package app.domain.services;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.enums.AccountStatus;
import app.domain.ports.BankAccountPort;
import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class CancelBankAccount {
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;

    public CancelBankAccount(BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    public void execute(String accountNumber, Long clientId) {
        BankAccount account = bankAccountPort.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("Cuenta no encontrada.");
        }
        
        // Regla de Negocio: No se puede cancelar una cuenta si tiene fondos o deudas
        if (account.getCurrentBalance().getAmount().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("El saldo debe ser exactamente cero para cancelar la cuenta.");
        }
        
        account.setAccountStatus(AccountStatus.CANCELED);
        bankAccountPort.update(account);
        
        logOperation.record(clientId, "CLIENT", "ACCOUNT_CANCELED", accountNumber, new HashMap<>());
    }
}