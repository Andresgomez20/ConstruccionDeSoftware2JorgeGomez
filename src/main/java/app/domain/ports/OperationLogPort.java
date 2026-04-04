package app.domain.ports;

import java.util.List;

import app.domain.models.entities.OperationLog;

public interface OperationLogPort {

    void save(OperationLog log);
    List<OperationLog> findByAffectedProductId(String affectedProductId);
    List<OperationLog> findByUserId(Long userId);

}