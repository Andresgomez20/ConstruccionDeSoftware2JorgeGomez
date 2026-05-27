package app.infrastructure.adapters.api.dtos;

/**
 * DTO para la respuesta de login con JWT
 */
public class LoginResponse {
    private String message;
    private String token;
    private String username;
    private String role;
    private String document;
    private String error;

    public LoginResponse() {}
    
    public LoginResponse(String message, String token, String username, String role, String document, String error) {
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
        this.document = document;
        this.error = error;
    }

    public LoginResponse(String message, String token, String username, String role, String document) {
        this.message = message;
        this.token = token;
        this.username = username;
        this.role = role;
        this.document = document;
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
