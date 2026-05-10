package app.infrastructure.adapters.persistence.mongodb;

import app.domain.models.entities.OperationLog;
import app.domain.ports.OperationLogPort;
import app.infrastructure.adapters.persistence.mongodb.entities.LogEntity;
import app.infrastructure.adapters.persistence.mongodb.repositories.LogRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LogPersistenceAdapter implements OperationLogPort {

    private final LogRepository repository;

    public LogPersistenceAdapter(LogRepository repository) {
        this.repository = repository;
    }

    // MÉTODOS DEL PUERTO
  
    @Override
    public void save(OperationLog log) {
        LogEntity entity = new LogEntity();
        
        // Mapeo
        entity.setUserId(log.getUserId());
        entity.setOperationType(log.getOperationType());
        entity.setUserType(log.getUserRole());             
        entity.setResourceId(log.getAffectedProductId());  
        entity.setTimestamp(log.getOperationDateTime());   
        entity.setDetails(log.getDetailData());            

        repository.save(entity);
    }

    @Override
    public List<OperationLog> findByAffectedProductId(String affectedProductId) {
        // 1. Buscamos en la base de datos
        List<LogEntity> entities = repository.findByResourceId(affectedProductId);
        
        // 2. Creamos la lista vacía
        List<OperationLog> domainLogs = new ArrayList<>();
        
        // 3. Recorremos y mapeamos uno por uno (¡Adiós streams!)
        for (LogEntity entity : entities) {
            domainLogs.add(toDomain(entity));
        }
        
        return domainLogs;
    }

    @Override
    public List<OperationLog> findByUserId(Long userId) {
        // 1. Buscamos en la base de datos
        List<LogEntity> entities = repository.findByUserId(userId);
        
        // 2. Creamos la lista vacía
        List<OperationLog> domainLogs = new ArrayList<>();
        
        // 3. Recorremos y mapeamos uno por uno
        for (LogEntity entity : entities) {
            domainLogs.add(toDomain(entity));
        }
        
        return domainLogs;
    }
  
    // MAPPER 
  
    private OperationLog toDomain(LogEntity entity) {
        if (entity == null) return null;
        
        OperationLog domain = new OperationLog();
        domain.setLogId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setOperationType(entity.getOperationType());
        domain.setUserRole(entity.getUserType());
        domain.setAffectedProductId(entity.getResourceId());
        domain.setOperationDateTime(entity.getTimestamp());
        domain.setDetailData(entity.getDetails());
        
        return domain;
    }
}