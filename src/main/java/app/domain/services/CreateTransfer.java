package app.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.entities.Transfer;
import app.domain.models.enums.TransferStatus;
import app.domain.models.enums.Role;
import app.domain.models.vo.Money; 
import app.domain.models.identity.User;
import app.domain.ports.BankAccountPort;
import app.domain.ports.TransferPort;
import app.domain.models.enums.AccountStatus;
import app.infrastructure.security.SecurityContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateTransfer {

    private final TransferPort transferPort;
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation; 
    
    // Umbral. Regla de negocio numérica general
    private static final BigDecimal APPROVAL_THRESHOLD = new BigDecimal("10000000.00"); 

    public CreateTransfer(TransferPort transferPort, BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    @Transactional
    public void execute(Transfer transfer) throws BusinessException {
        // 1. Validaciones imperativas de Null-Safety
        if (transfer == null) {
            throw new BusinessException("Los datos de la transferencia no pueden ser nulos.");
        }
        if (transfer.getOriginAccount() == null || transfer.getDestinationAccount() == null) {
            throw new BusinessException("Las cuentas de origen y destino son obligatorias.");
        }
        if (transfer.getAmount() == null || transfer.getAmount().getAmount() == null) {
            throw new BusinessException("El monto de la transferencia debe estar definido.");
        }

        // 2. Obtener el usuario autenticado
        User currentUser = SecurityContext.getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException("Usuario no autenticado.");
        }

        boolean isCompanyUser = isUserCompanyRole(currentUser.getRole());

        // Extraemos el número puro del Value Object Money de forma segura
        BigDecimal montoTransferencia = transfer.getAmount().getAmount();

        // 3. Regla de Negocio: Monto válido
        if (montoTransferencia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto de la transferencia debe ser estrictamente mayor que cero.");
        }

        // 4. Regla de Negocio: Validar estado de AMBAS cuentas (Operativas)
        BankAccount origin = bankAccountPort.findByAccountNumber(transfer.getOriginAccount());
        if (origin == null) {
            throw new BusinessException("La cuenta de origen no fue encontrada.");
        }
        if (origin.getAccountStatus() == AccountStatus.BLOCKED || origin.getAccountStatus() == AccountStatus.CANCELED) {
            throw new BusinessException("La cuenta de origen está bloqueada o cancelada. No se permiten operaciones.");
        }

        BankAccount destination = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount());
        if (destination == null) {
            throw new BusinessException("La cuenta de destino no fue encontrada.");
        }
        if (destination.getAccountStatus() == AccountStatus.BLOCKED || destination.getAccountStatus() == AccountStatus.CANCELED) {
            throw new BusinessException("La cuenta de destino está bloqueada o cancelada. No se permiten operaciones.");
        }

        // 5. Regla de Negocio: Disponibilidad de Fondos
        BigDecimal saldoAntesOrigen = origin.getCurrentBalance().getAmount();
        if (saldoAntesOrigen.compareTo(montoTransferencia) < 0) {
            throw new BusinessException("Fondos insuficientes en la cuenta de origen.");
        }

        // Preparar datos comunes de la transferencia
        transfer.setCreationDate(LocalDateTime.now());
        transfer.setCreatorUserId(currentUser.getId());

        // 6. FLUJO DE NEGOCIO: Evaluación del Umbral
        if (isCompanyUser && montoTransferencia.compareTo(APPROVAL_THRESHOLD) >= 0) {
            
            // RAMA A: En espera de aprobación (No se mueve dinero)
            transfer.setStatus(TransferStatus.PENDING_APPROVAL);
            transferPort.save(transfer);
            
            Map<String, Object> details = new HashMap<>();
            details.put("monto", montoTransferencia);
            details.put("origen", transfer.getOriginAccount());
            details.put("destino", transfer.getDestinationAccount());
            details.put("requiereAprobacion", true);
            details.put("umbralAplicado", APPROVAL_THRESHOLD);
            
            logOperation.record(
                currentUser.getId(), 
                currentUser.getRole().name(), 
                "TRANSFERENCIA_EN_ESPERA_APROBACION", 
                transfer.getOriginAccount(), 
                details
            );
            
        } else {
            
            // RAMA B: Ejecución Inmediata
            transfer.setStatus(TransferStatus.EXECUTED);
            
            BigDecimal saldoAntesDestino = destination.getCurrentBalance().getAmount();
            
            // Matemática financiera pura
            BigDecimal nuevoSaldoOrigen = saldoAntesOrigen.subtract(montoTransferencia);
            BigDecimal nuevoSaldoDestino = saldoAntesDestino.add(montoTransferencia);
            
            // Reconstruimos los Value Objects Money como exige tu clase BankAccount
            origin.setCurrentBalance(new Money(nuevoSaldoOrigen, origin.getCurrentBalance().getCurrency()));
            destination.setCurrentBalance(new Money(nuevoSaldoDestino, destination.getCurrentBalance().getCurrency()));
            
            bankAccountPort.update(origin);
            bankAccountPort.update(destination);
            transferPort.save(transfer);

            // Registro Exacto solicitado por el documento del Banco
            Map<String, Object> details = new HashMap<>();
            details.put("montoInvolucrado", montoTransferencia);
            details.put("saldoAntesOrigen", saldoAntesOrigen);
            details.put("saldoDespuesOrigen", nuevoSaldoOrigen);
            details.put("saldoAntesDestino", saldoAntesDestino);
            details.put("saldoDespuesDestino", nuevoSaldoDestino);
            
            logOperation.record(
                currentUser.getId(), 
                currentUser.getRole().name(), 
                "TRANSFERENCIA_EJECUTADA_INMEDIATA", 
                transfer.getOriginAccount(), 
                details
            );
        }
    }

    /**
     * Determina si un rol corresponde a un usuario de empresa
     */
    private boolean isUserCompanyRole(Role role) {
        return role == Role.CLIENT_COMPANY || 
               role == Role.COMPANY_OPERATOR || 
               role == Role.COMPANY_SUPERVISOR;
    }
}