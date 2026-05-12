package app.infrastructure.adapters.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para la respuesta de login con JWT
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String message;
    private String token;
    private String username;
    private String role;
    private String document;
    private String error;

    public LoginResponse(String message, String token, String username, String role, String document) {
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
        this.document = document;
    }
}
