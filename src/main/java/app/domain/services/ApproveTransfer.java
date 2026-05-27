package app.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.entities.Transfer;
import app.domain.models.enums.AccountStatus; 
import app.domain.models.enums.TransferStatus;
import app.domain.models.identity.User;
import app.domain.models.vo.Money;
import app.domain.ports.BankAccountPort;
import app.domain.ports.TransferPort;
import app.infrastructure.security.SecurityContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApproveTransfer {

    private final TransferPort transferPort;
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation;
    private final AuthorizationService authorizationService;

    public ApproveTransfer(TransferPort transferPort, BankAccountPort bankAccountPort, 
                           LogOperation logOperation, AuthorizationService authorizationService) {
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
        this.authorizationService = authorizationService;
    }

    /**
     * Aprueba o rechaza una transferencia validando que el usuario sea COMPANY_SUPERVISOR
     */
    @Transactional
    public void execute(Long transferId, boolean approve) throws BusinessException {
        // 1. Obtener usuario y validar permisos
        User currentUser = SecurityContext.getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException("Usuario no autenticado.");
        }
        authorizationService.validateTransferApprovalPermission(currentUser);

        // 2. Obtener la transferencia
        Transfer transfer = transferPort.findById(transferId);
        if (transfer == null) {
            throw new BusinessException("Transferencia no encontrada.");
        }

        if (transfer.getStatus() != TransferStatus.PENDING_APPROVAL) {
            throw new BusinessException("La transferencia no está pendiente de aprobación.");
        }
        
        // ESCENARIO 1: EXPIRACIÓN TARDÍA
        if (transfer.getCreationDate().plusHours(1).isBefore(LocalDateTime.now())) {
            transfer.setStatus(TransferStatus.EXPIRED);
            transferPort.update(transfer);
            
            logOperation.record(
                currentUser.getId(), 
                currentUser.getRole().name(), 
                "TRANSFERENCIA_VENCIDA", 
                transferId.toString(), 
                Map.of("motivo", "Falta de aprobación en el tiempo establecido (1 hora)", 
                       "idUsuarioCreador", transfer.getCreatorUserId()) // <-- Adaptado a lo que pide el doc
            );
            
            throw new BusinessException("La transferencia expiró por falta de aprobación dentro de la hora límite.");
        }

        // ESCENARIO 2: RECHAZO MANUAL
        if (!approve) {
            transfer.setStatus(TransferStatus.REJECTED);
            transfer.setApproverUserId(currentUser.getId());
            transfer.setApprovalDate(LocalDateTime.now());
            transferPort.update(transfer);

            logOperation.record(
                currentUser.getId(), 
                currentUser.getRole().name(), 
                "TRANSFERENCIA_RECHAZADA", 
                transferId.toString(), 
                Map.of("monto", transfer.getAmount(), 
                       "cuentaOrigen", transfer.getOriginAccount(),
                       "idAnalistaAprobador", currentUser.getId())
            );
            return; // Salimos aquí si fue rechazada
        }

        // ESCENARIO 3: APROBACIÓN Y EJECUCIÓN
        BankAccount origin = bankAccountPort.findByAccountNumber(transfer.getOriginAccount());
        BankAccount destination = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount());

        if (origin == null || destination == null) {
            throw new BusinessException("Una de las cuentas involucradas no existe.");
        }

        if (origin.getAccountStatus() == AccountStatus.BLOCKED || origin.getAccountStatus() == AccountStatus.CANCELED) {
            throw new BusinessException("La cuenta de origen está bloqueada o cancelada. No se permiten operaciones.");
        }
        if (destination.getAccountStatus() == AccountStatus.BLOCKED || destination.getAccountStatus() == AccountStatus.CANCELED) {
            throw new BusinessException("La cuenta de destino está bloqueada o cancelada.");
        }

        // Extraemos los números de los objetos Money para poder hacer cálculos
        BigDecimal montoTransferencia = transfer.getAmount().getAmount();
        BigDecimal saldoAntesOrigen = origin.getCurrentBalance().getAmount();
        BigDecimal saldoAntesDestino = destination.getCurrentBalance().getAmount();

        if (saldoAntesOrigen.compareTo(montoTransferencia) < 0) {
            throw new BusinessException("Fondos insuficientes en la cuenta de origen al momento de la aprobación.");
        }

        BigDecimal nuevoSaldoOrigen = saldoAntesOrigen.subtract(montoTransferencia);
        BigDecimal nuevoSaldoDestino = saldoAntesDestino.add(montoTransferencia);
        
        
        // Reconstruimos el objeto Money con el nuevo valor (BigDecimal) y la moneda original
        origin.setCurrentBalance(new Money(nuevoSaldoOrigen, origin.getCurrentBalance().getCurrency()));
        destination.setCurrentBalance(new Money(nuevoSaldoDestino, destination.getCurrentBalance().getCurrency()));
        
        // Guardar cuentas
        bankAccountPort.update(origin);
        bankAccountPort.update(destination);

        // Guardar transferencia ejecutada
        transfer.setStatus(TransferStatus.EXECUTED);
        transfer.setApproverUserId(currentUser.getId());
        transfer.setApprovalDate(LocalDateTime.now());
        transferPort.update(transfer);

        // REGISTRO PERFECTO EN BITÁCORA PARA EJECUCIÓN
        Map<String, Object> details = new HashMap<>();
        details.put("montoInvolucrado", montoTransferencia);
        details.put("saldoAntesOrigen", saldoAntesOrigen);
        details.put("saldoDespuesOrigen", nuevoSaldoOrigen);
        details.put("saldoAntesDestino", saldoAntesDestino);
        details.put("saldoDespuesDestino", nuevoSaldoDestino);
        details.put("idAnalistaAprobador", currentUser.getId());

        logOperation.record(
            currentUser.getId(), 
            currentUser.getRole().name(), 
            "TRANSFERENCIA_EJECUTADA", 
            transferId.toString(), 
            details
        );
    }
}