package app.domain.services;

import app.domain.Exceptions.BusinessException;
import app.domain.models.identity.User;
import app.domain.ports.UserPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación de usuarios
 */
@Service
public class AuthenticationService {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserPort userPort, PasswordEncoder passwordEncoder) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    //Autentica un usuario con username y password
    
    public User authenticate(String username, String password) throws BusinessException {
        // Validar que los parámetros no sean nulos
        if (username == null || username.isBlank()) {
            throw new BusinessException("El nombre de usuario no puede estar vacío.");
        }
        if (password == null || password.isBlank()) {
            throw new BusinessException("La contraseña no puede estar vacía.");
        }

        // Buscar el usuario por username
        User user = userPort.findByUsername(username);
        if (user == null) {
            throw new BusinessException("Usuario o contraseña incorrectos.");
        }

        // Validar la contraseña
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("Usuario o contraseña incorrectos.");
        }

        return user;
    }
}
