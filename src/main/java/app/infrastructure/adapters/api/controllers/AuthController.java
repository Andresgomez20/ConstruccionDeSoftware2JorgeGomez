package app.infrastructure.adapters.api.controllers;

import app.domain.Exceptions.BusinessException;
import app.domain.models.identity.User;
import app.domain.services.AuthenticationService;
import app.infrastructure.adapters.api.dtos.LoginRequest;
import app.infrastructure.adapters.api.dtos.LoginResponse;
import app.infrastructure.security.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de autenticación y autorización
 * Maneja login y generación de JWT
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtProvider jwtProvider;

    public AuthController(AuthenticationService authenticationService, JwtProvider jwtProvider) {
        this.authenticationService = authenticationService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Endpoint de login
     * Autentica un usuario y retorna un JWT con username, role y documento
     *
     * @param loginRequest DTO con username y password
     * @return JWT token si las credenciales son válidas
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Validación imperativa clásica
            if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                return new ResponseEntity<>(
                    new LoginResponse("Error", null, null, null, null, "Username y password son requeridos."),
                    HttpStatus.BAD_REQUEST
                );
            }

            // Autenticar al usuario
            User authenticatedUser = authenticationService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            );

            // Generar JWT con username, role y documento
            String token = jwtProvider.generateToken(
                authenticatedUser.getUsername(),
                authenticatedUser.getRole().name(),
                authenticatedUser.getIdentificationId()
            );

            // Construir respuesta exitosa
            LoginResponse response = new LoginResponse();
            response.setMessage("Autenticación exitosa");
            response.setToken(token);
            response.setUsername(authenticatedUser.getUsername());
            response.setRole(authenticatedUser.getRole().name());
            response.setDocument(authenticatedUser.getIdentificationId());

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (BusinessException e) {
            return new ResponseEntity<>(
                new LoginResponse("Error", null, null, null, null, e.getMessage()),
                HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                new LoginResponse("Error", null, null, null, null, "Error interno del servidor: " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Endpoint para validar un token JWT
     *
     * @param token JWT token a validar
     * @return Estado de validación del token
     */
    @GetMapping("/validate/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token) {
        try {
            if (jwtProvider.validateToken(token)) {
                return new ResponseEntity<>(
                    new ValidationResponse(
                        "Token válido",
                        true,
                        jwtProvider.getUsernameFromToken(token),
                        jwtProvider.getRoleFromToken(token),
                        jwtProvider.getDocumentFromToken(token)
                    ),
                    HttpStatus.OK
                );
            } else {
                return new ResponseEntity<>(
                    new ValidationResponse("Token inválido o expirado", false, null, null, null),
                    HttpStatus.UNAUTHORIZED
                );
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                new ValidationResponse("Error al validar token: " + e.getMessage(), false, null, null, null),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * Clase interna para respuesta de validación de token
     * (Pública y con Getters para que Jackson pueda armar el JSON)
     */
    public static class ValidationResponse {
        private String message;
        private boolean valid;
        private String username;
        private String role;
        private String document;

        public ValidationResponse(String message, boolean valid, String username, String role, String document) {
            this.message = message;
            this.valid = valid;
            this.username = username;
            this.role = role;
            this.document = document;
        }

        // Getters clásicos que eliminan los warnings de uso
        public String getMessage() {
            return message;
        }

        public boolean isValid() {
            return valid;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }

        public String getDocument() {
            return document;
        }
    }
}