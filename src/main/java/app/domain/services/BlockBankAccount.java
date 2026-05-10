package app.domain.services;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.enums.AccountStatus;
import app.domain.ports.BankAccountPort;
import java.util.HashMap;

@Service
public class BlockBankAccount {
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;

    public BlockBankAccount(BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    public void execute(String accountNumber, Long analystId, String reason) {
        BankAccount account = bankAccountPort.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new BusinessException("Cuenta no encontrada.");
        }
        
        account.setAccountStatus(AccountStatus.BLOCKED);
        bankAccountPort.update(account);
        
        HashMap<String, Object> details = new HashMap<>();
        details.put("reason", reason);
        logOperation.record(analystId, "INTERNAL_ANALYST", "ACCOUNT_BLOCKED", accountNumber, details);
    }
}