package app.application.usecases;

import app.domain.Exceptions.BusinessException;
import app.domain.models.identity.User;
import app.domain.services.CreateUser;
import app.domain.ports.UserPort; // Importamos el puerto para las consultas
import org.springframework.stereotype.Service;

@Service
public class UserUseCase {

    private final CreateUser createUser;
    private final UserPort userPort;

    public UserUseCase(CreateUser createUser, UserPort userPort) {
        this.createUser = createUser;
        this.userPort = userPort;
    }

    /**
     * Registra un usuario en el sistema delegando al servicio del dominio
     */
    public void registerUser(User user) throws BusinessException {
        createUser.execute(user);
    }

    /**
     * Busca un usuario por su nombre de usuario (username)
     */
    public User findByUsername(String username) {
        return userPort.findByUsername(username);
    }

    /**
     * Busca un usuario por su número de identificación
     */
    public User findByIdentificationId(String identificationId) {
        return userPort.findByIdentificationId(identificationId);
    }
}