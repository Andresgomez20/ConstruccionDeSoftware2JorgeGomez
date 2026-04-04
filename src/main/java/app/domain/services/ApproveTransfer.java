package app.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.entities.Transfer;
import app.domain.models.enums.TransferStatus;
import app.domain.ports.BankAccountPort;
import app.domain.ports.TransferPort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApproveTransfer {

    private final TransferPort transferPort;
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation; 

    public ApproveTransfer(TransferPort transferPort, BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    @Transactional
    public void approveOrReject(Long transferId, boolean approve, Long approverUserId) {
        Transfer transfer = transferPort.findById(transferId);
        if (transfer == null) {
            throw new BusinessException("Transferencia no encontrada.");
        }

        if (transfer.getStatus() != TransferStatus.PENDING_APPROVAL) {
            throw new BusinessException("La transferencia no está pendiente de aprobación.");
        }
        
        // ESCENARIO 1: EXPIRACIÓN
        if (transfer.getCreationDate().plusHours(1).isBefore(LocalDateTime.now())) {
            transfer.setStatus(TransferStatus.EXPIRED);
            transferPort.update(transfer);
            
            logOperation.record(
                approverUserId, 
                "SYSTEM/SUPERVISOR", 
                "TRANSFER_EXPIRED", 
                transferId.toString(), 
                Map.of("reason", "Tiempo agotado: Se excedió el límite de 1 hora.")
            );
            
            throw new BusinessException("Transferencia expirada por falta de aprobación dentro de 1 hora.");
        }

        // ESCENARIO 2: RECHAZO MANUAL
        if (!approve) {
            transfer.setStatus(TransferStatus.REJECTED);
            transfer.setApproverUserId(approverUserId);
            transfer.setApprovalDate(LocalDateTime.now());
            transferPort.update(transfer);

            logOperation.record(
                approverUserId, 
                "COMPANY_SUPERVISOR", 
                "TRANSFER_REJECTED", 
                transferId.toString(), 
                Map.of("amount", transfer.getAmount(), "origin", transfer.getOriginAccount())
            );
            return;
        }

        BankAccount origin = bankAccountPort.findByAccountNumber(transfer.getOriginAccount());
        BankAccount destination = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount());

        if (origin.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
             throw new BusinessException("Fondos insuficientes en la cuenta de origen al momento de la aprobación.");
        }

        // Ejecutar transferencia en MySQL
        origin.setCurrentBalance(origin.getCurrentBalance().subtract(transfer.getAmount()));
        destination.setCurrentBalance(destination.getCurrentBalance().add(transfer.getAmount()));
        
        bankAccountPort.update(origin);
        bankAccountPort.update(destination);

        transfer.setStatus(TransferStatus.EXECUTED);
        transfer.setApproverUserId(approverUserId);
        transfer.setApprovalDate(LocalDateTime.now());
        transferPort.update(transfer);

        // ESCENARIO 3: EJECUCIÓN EXITOSA
        Map<String, Object> details = new HashMap<>();
        details.put("monto", transfer.getAmount());
        details.put("cuentaOrigen", transfer.getOriginAccount());
        details.put("cuentaDestino", transfer.getDestinationAccount());
        details.put("nuevoSaldoOrigen", origin.getCurrentBalance());
        details.put("statusFinal", "EXECUTED");

        logOperation.record(
            approverUserId, 
            "COMPANY_SUPERVISOR", 
            "TRANSFER_APPROVED_AND_EXECUTED", 
            transferId.toString(), 
            details
        );
    }
}