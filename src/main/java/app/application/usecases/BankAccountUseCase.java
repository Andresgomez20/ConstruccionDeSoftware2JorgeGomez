package app.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.services.CreateBankAccount;
import app.domain.services.BlockBankAccount;
import app.domain.services.CancelBankAccount;

@Service
public class BankAccountUseCase {

    @Autowired
    private CreateBankAccount createBankAccount;
    
    @Autowired
    private BlockBankAccount blockBankAccount;
    
    @Autowired
    private CancelBankAccount cancelBankAccount;

    public BankAccountUseCase(CreateBankAccount createBankAccount, 
                              BlockBankAccount blockBankAccount, 
                              CancelBankAccount cancelBankAccount) {
        this.createBankAccount = createBankAccount;
        this.blockBankAccount = blockBankAccount;
        this.cancelBankAccount = cancelBankAccount;
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
}