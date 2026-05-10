package app.infrastructure.adapters.persistence.mongodb.repositories;

import app.infrastructure.adapters.persistence.mongodb.entities.LogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LogRepository extends MongoRepository<LogEntity, String> {
    
    List<LogEntity> findByUserId(Long userId);
    
    // Mongo buscará por el campo resourceId de nuestra LogEntity
    List<LogEntity> findByResourceId(String resourceId); 
}