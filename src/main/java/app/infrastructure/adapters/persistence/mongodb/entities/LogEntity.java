package app.infrastructure.adapters.persistence.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "operation_logs")
@Getter
@Setter
public class LogEntity {
    @Id
    private String id;
    private Long userId;
    private String userType;
    private String operationType;
    private String resourceId;
    private LocalDateTime timestamp;
    private Map<String, Object> details;
}