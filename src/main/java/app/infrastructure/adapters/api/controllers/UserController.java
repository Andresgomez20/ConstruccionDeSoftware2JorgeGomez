package app.infrastructure.adapters.api.controllers;

import app.application.usecases.UserUseCase;
import app.domain.models.identity.User;
import app.domain.Exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    // Endpoint para registrar un nuevo usuario (ADMIN, ANALYST, etc.)
    
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            userUseCase.registerUser(user);
            return new ResponseEntity<>("Usuario registrado exitosamente en el sistema", HttpStatus.CREATED);
        } catch (BusinessException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}