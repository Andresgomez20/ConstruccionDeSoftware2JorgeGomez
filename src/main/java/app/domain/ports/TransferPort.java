package app.domain.ports;

import java.util.List;

import app.domain.models.entities.Transfer;
import app.domain.models.enums.TransferStatus;

public interface TransferPort {

    void save(Transfer transfer);
    void update(Transfer transfer);
    Transfer findById(Long id);
    List<Transfer> findByOriginAccount(String originAccount);
    List<Transfer> findByStatus(TransferStatus status); // Útil para buscar transferencias vencidas o pendientes
    
}