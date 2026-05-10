package app.domain.services;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.Transfer;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.TransferPort;
import java.util.HashMap;

@Service
public class ExpireTransfer {
    private final TransferPort transferPort;
    private final LogOperation logOperation;

    public ExpireTransfer(TransferPort transferPort, LogOperation logOperation) {
        this.transferPort = transferPort;
        this.logOperation = logOperation;
    }

    public void execute(Long transferId) {
        Transfer transfer = transferPort.findById(transferId);
        if (transfer == null) throw new BusinessException("Transferencia no encontrada.");
        
        if (transfer.getStatus() != TransferStatus.PENDING_APPROVAL) {
            throw new BusinessException("Solo las transferencias pendientes pueden expirar.");
        }

        transfer.setStatus(TransferStatus.EXPIRED); 
        transferPort.update(transfer);

        HashMap<String, Object> details = new HashMap<>();
        details.put("reason", "Tiempo de aprobación excedido");
        logOperation.record(0L, "SYSTEM_SCHEDULER", "TRANSFER_EXPIRED", transferId.toString(), details);
    }
}