package app.domain.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class OperationLog {

    private String logId;           // ID generado por MongoDB
    private String operationType;    
    private LocalDateTime operationDateTime;
    private Long userId;            // ID del usuario que operó
    private String userRole;        // Rol del usuario
    private String affectedProductId; // ID del producto (Cuenta, Préstamo, etc.)
    
    // Guardar los detalles dinámicos de cada operación
    private Map<String, Object> detailData; 
}