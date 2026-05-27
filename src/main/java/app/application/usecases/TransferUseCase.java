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

    /**
     * Crea una transferencia
     * El usuario se obtiene del contexto de seguridad (JWT)
     */
    public void createTransfer(Transfer transfer) throws BusinessException {
        createTransfer.execute(transfer);
    }

    /**
     * Aprueba o rechaza una transferencia
     * Requiere que el usuario sea COMPANY_SUPERVISOR
     */
    public void approveTransfer(Long transferId, boolean approve) throws BusinessException {
        approveTransfer.execute(transferId, approve);
    }

    /**
     * Expira una transferencia
     */
    public void expireTransfer(Long transferId) throws BusinessException {
        expireTransfer.execute(transferId);
    }
}