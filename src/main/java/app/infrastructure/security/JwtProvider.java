package app.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Proveedor de JWT (JSON Web Tokens) para autenticación y autorización
 */
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * Genera un JWT con los datos del usuario
     *
     * @param username         Nombre de usuario
     * @param role             Rol del usuario
     * @param identificationId Documento de identificación del usuario
     * @return Token JWT
     */
    public String generateToken(String username, String role, String identificationId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("document", identificationId);

        return createToken(claims, username);
    }

    /**
     * Crea un token JWT con los claims especificados usando la API moderna (0.12+)
     *
     * @param claims   Mapa de claims adicionales
     * @param subject  Usuario (subject del token)
     * @return Token JWT generado
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // Usamos la nueva sintaxis fluida sin el prefijo "set"
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key) // El algoritmo se infiere automáticamente de la llave
                .compact();
    }

    /**
     * Extrae el usuario (subject) del token
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrae el rol del token
     */
    public String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    /**
     * Extrae el documento del token
     */
    public String getDocumentFromToken(String token) {
        return (String) getClaims(token).get("document");
    }

    /**
     * Valida si el token es válido
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae los claims del token
     */
    private Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}