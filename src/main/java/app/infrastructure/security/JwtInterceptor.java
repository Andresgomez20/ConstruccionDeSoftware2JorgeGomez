package app.infrastructure.security;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import app.domain.models.identity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor HTTP que extrae el JWT y establece el usuario autenticado
 * Se ejecuta antes de cada solicitud para poblador el SecurityContext
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    public JwtInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        try {
            // Obtener el token del header Authorization
            String authHeader = request.getHeader("Authorization");
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // Remover "Bearer "

                // Validar el token
                if (jwtProvider.validateToken(token)) {
                    // Extraer datos del token
                    String username = jwtProvider.getUsernameFromToken(token);
                    String role = jwtProvider.getRoleFromToken(token);
                    String document = jwtProvider.getDocumentFromToken(token);

                    // Crear usuario desde el JWT
                    User user = SecurityContext.createUserFromJwt(username, role, document);
                    
                    // Establecer en el contexto de seguridad
                    SecurityContext.setCurrentUser(user);
                }
            }
        } catch (Exception e) {
            // Si hay error al procesar el JWT, continuar sin usuario autenticado
            // El validador de autorización se encargará de rechazar operaciones que lo requieran
        }

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
        // Limpiar el contexto de seguridad después de la solicitud
        SecurityContext.clear();
    }
}