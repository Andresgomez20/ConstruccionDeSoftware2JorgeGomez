package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.enums.UserStatus;
import app.domain.models.identity.User;
import app.domain.ports.UserPort;

import java.util.HashMap;
import java.util.Map;

@Service
public class CreateUser {

    private final UserPort userPort;
    private final LogOperation logOperation;

    @Autowired
    public CreateUser(UserPort userPort, LogOperation logOperation) {
        this.userPort = userPort;
        this.logOperation = logOperation;
    }

    public void createUser(User user) throws BusinessException {
        // Validaciones de unicidad
        if (userPort.existsByIdentificationId(user.getIdentificationId())) {
            throw new BusinessException("Ya existe un usuario con este ID de identificación.");
        }
        if (userPort.existsByUsername(user.getUsername())) {
            throw new BusinessException("Ya existe un usuario con este nombre de usuario.");
        }
        if (userPort.existsByEmail(user.getEmail())) {
            throw new BusinessException("Ya existe un usuario con este correo electrónico.");
        }

        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        // Guardar en MySQL
        userPort.save(user);

        // REGISTRO EN BITÁCORA
        Map<String, Object> details = new HashMap<>();
        details.put("username", user.getUsername());
        details.put("role", user.getRole());
        details.put("email", user.getEmail());
        details.put("statusAlCrear", user.getStatus());

        logOperation.record(
            0L, // ID 0 para representar que es una creación de cuenta nueva
            user.getRole().toString(), 
            "USER_REGISTERED", 
            user.getIdentificationId(), 
            details
        );
    }
}