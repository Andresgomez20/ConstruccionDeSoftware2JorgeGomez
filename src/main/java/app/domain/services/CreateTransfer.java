package app.domain.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.entities.Transfer;
import app.domain.models.enums.TransferStatus;
import app.domain.models.vo.Money;
import app.domain.ports.BankAccountPort;
import app.domain.ports.TransferPort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateTransfer {

    private final TransferPort transferPort;
    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation; 
    
    // Umbral. Lo dejamos como BigDecimal puro porque es una regla de negocio numérica general
    private static final BigDecimal APPROVAL_THRESHOLD = new BigDecimal("10000.00"); 

    public CreateTransfer(TransferPort transferPort, BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.transferPort = transferPort;
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    @Transactional
    public void create(Transfer transfer, boolean isCompanyUser) {
        // Corrección 1: Extraemos el valor numérico (.getAmount()) solo para validar que no sea 0 o negativo
        if (transfer.getAmount() == null || transfer.getAmount().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto de la transferencia debe ser estrictamente mayor que cero.");
        }

        BankAccount origin = bankAccountPort.findByAccountNumber(transfer.getOriginAccount());
        if (origin == null || origin.getAccountStatus().equals("BLOCKED") || origin.getAccountStatus().equals("CANCELED")) {
            throw new BusinessException("La cuenta de origen es inválida, bloqueada o cancelada.");
        }

        BankAccount destination = bankAccountPort.findByAccountNumber(transfer.getDestinationAccount());
        if (destination == null) {
            throw new BusinessException("Cuenta de destino no encontrada.");
        }

        // Corrección 2: Aquí usamos el compareTo que creaste en la clase Money
        if (origin.getCurrentBalance().compareTo(transfer.getAmount()) < 0) {
            throw new BusinessException("Fondos insuficientes en la cuenta de origen.");
        }

        transfer.setCreationDate(LocalDateTime.now());

        // MAPA DE DETALLES PARA MONGODB
        Map<String, Object> details = new HashMap<>();
        details.put("monto", transfer.getAmount()); // Mongo guardará el objeto Money (monto y moneda)
        details.put("origen", transfer.getOriginAccount());
        details.put("destino", transfer.getDestinationAccount());
        details.put("esEmpresa", isCompanyUser);

        // Corrección 3: Extraemos el valor numérico para compararlo con el umbral de 10,000
        if (isCompanyUser && transfer.getAmount().getAmount().compareTo(APPROVAL_THRESHOLD) >= 0) {
            transfer.setStatus(TransferStatus.PENDING_APPROVAL);
            
            // Log de espera de aprobación
            details.put("requiereAprobacion", true);
            details.put("umbralAplicado", APPROVAL_THRESHOLD);
            
            logOperation.record(
                transfer.getCreatorUserId(), 
                "COMPANY_USER", 
                "TRANSFER_QUEUED_FOR_APPROVAL", 
                transfer.getOriginAccount(), 
                details
            );
        } else {
            // Ejecución directa
            transfer.setStatus(TransferStatus.EXECUTED);
            executeTransfer(origin, destination, transfer.getAmount());

            // Log de ejecución inmediata
            details.put("requiereAprobacion", false);
            details.put("nuevoSaldoOrigen", origin.getCurrentBalance());
            
            logOperation.record(
                transfer.getCreatorUserId(), 
                isCompanyUser ? "COMPANY_USER" : "NATURAL_USER", 
                "TRANSFER_EXECUTED_IMMEDIATELY", 
                transfer.getOriginAccount(), 
                details
            );
        }

        transferPort.save(transfer);
    }

    // Corrección 4: Cambiamos el parámetro "BigDecimal" por "Money"
    private void executeTransfer(BankAccount origin, BankAccount destination, Money amount) {
        // Corrección 5: Ahora las sumas y restas usan tu objeto Money directamente
        origin.setCurrentBalance(origin.getCurrentBalance().subtract(amount));
        destination.setCurrentBalance(destination.getCurrentBalance().add(amount));
        
        bankAccountPort.update(origin);
        bankAccountPort.update(destination);
    }
}