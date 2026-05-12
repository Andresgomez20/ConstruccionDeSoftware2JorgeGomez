package app.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
     * Crea un token JWT con los claims especificados
     *
     * @param claims   Mapa de claims adicionales
     * @param subject  Usuario (subject del token)
     * @return Token JWT generado
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el usuario (subject) del token
     *
     * @param token JWT token
     * @return Username del token
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrae el rol del token
     *
     * @param token JWT token
     * @return Rol del usuario
     */
    public String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    /**
     * Extrae el documento del token
     *
     * @param token JWT token
     * @return Documento de identificación del usuario
     */
    public String getDocumentFromToken(String token) {
        return (String) getClaims(token).get("document");
    }

    /**
     * Valida si el token es válido
     *
     * @param token JWT token
     * @return true si el token es válido, false en caso contrario
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
     *
     * @param token JWT token
     * @return Claims del token
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
