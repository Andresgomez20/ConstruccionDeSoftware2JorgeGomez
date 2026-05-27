package app.application.usecases;

import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.services.CreateBankAccount;
import app.domain.services.BlockBankAccount;
import app.domain.services.CancelBankAccount;
import app.domain.ports.BankAccountPort;

@Service
public class BankAccountUseCase {

    private final CreateBankAccount createBankAccount;
    private final BlockBankAccount blockBankAccount;
    private final CancelBankAccount cancelBankAccount;
    private final BankAccountPort bankAccountPort;

    // Spring Boot inyecta todo aquí
    public BankAccountUseCase(CreateBankAccount createBankAccount, 
                              BlockBankAccount blockBankAccount, 
                              CancelBankAccount cancelBankAccount, 
                              BankAccountPort bankAccountPort) {
        this.createBankAccount = createBankAccount;
        this.blockBankAccount = blockBankAccount;
        this.cancelBankAccount = cancelBankAccount;
        this.bankAccountPort = bankAccountPort;
    }

    public void createBankAccount(BankAccount bankAccount) throws BusinessException {
        createBankAccount.execute(bankAccount);
    }
    
    public void blockBankAccount(String accountNumber, Long analystId, String reason) throws BusinessException {
        blockBankAccount.execute(accountNumber, analystId, reason);
    }
    
    public void cancelBankAccount(String accountNumber, Long clientId) throws BusinessException {
        cancelBankAccount.execute(accountNumber, clientId);
    }

    public BankAccount findByAccountNumber(String accountNumber) {
        return bankAccountPort.findByAccountNumber(accountNumber);
    }
}