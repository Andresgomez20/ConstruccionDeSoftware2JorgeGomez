package app.domain.services;

import org.springframework.stereotype.Service;

import app.domain.models.entities.OperationLog;
import app.domain.ports.OperationLogPort;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class LogOperation {

    private final OperationLogPort operationLogPort;

    public LogOperation(OperationLogPort operationLogPort) {
        this.operationLogPort = operationLogPort;
    }

    public void record(Long userId, String role, String type, String productId, Map<String, Object> details) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUserRole(role);
        log.setOperationType(type);
        log.setAffectedProductId(productId);
        log.setOperationDateTime(LocalDateTime.now());
        log.setDetailData(details);

        operationLogPort.save(log);
    }
}