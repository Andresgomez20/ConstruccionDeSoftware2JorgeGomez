package app.application.usecases;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.Transfer;
import app.domain.services.CreateTransfer;
import app.domain.services.ApproveTransfer;
import app.domain.services.ExpireTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferUseCase {

    @Autowired
    private CreateTransfer createTransfer;
    
    @Autowired
    private ApproveTransfer approveTransfer;
    
    @Autowired
    private ExpireTransfer expireTransfer;

    public TransferUseCase(CreateTransfer createTransfer, ApproveTransfer approveTransfer, ExpireTransfer expireTransfer) {
        this.createTransfer = createTransfer;
        this.approveTransfer = approveTransfer;
        this.expireTransfer = expireTransfer;
    }

    public void createTransfer(Transfer transfer, boolean isCompanyUser) throws BusinessException {
        createTransfer.execute(transfer, isCompanyUser);
    }

    public void approveTransfer(Long transferId, boolean isCompanyUser, Long analystId) throws BusinessException {
        approveTransfer.execute(transferId, isCompanyUser, analystId);
    }

    public void expireTransfer(Long transferId) throws BusinessException {
        expireTransfer.execute(transferId);
    }
}