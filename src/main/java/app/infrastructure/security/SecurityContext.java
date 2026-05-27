package app.infrastructure.security;

import app.domain.models.identity.User;
import app.domain.models.enums.Role;
import app.domain.models.enums.UserStatus;

/**
 * Contexto de seguridad que mantiene el usuario autenticado actual
 * Utiliza ThreadLocal para mantener el usuario en el contexto de ejecución
 */
public class SecurityContext {
    private static final ThreadLocal<User> userContext = new ThreadLocal<>();

    /**
     * Establece el usuario actual en el contexto de seguridad
     */
    public static void setCurrentUser(User user) {
        userContext.set(user);
    }

    /**
     * Obtiene el usuario actual del contexto de seguridad
     * 
     * @return Usuario autenticado o null si no hay usuario
     */
    public static User getCurrentUser() {
        return userContext.get();
    }

    /**
     * Obtiene el ID del usuario actual
     * 
     * @return ID del usuario o null
     */
    public static Long getCurrentUserId() {
        User user = userContext.get();
        return user != null ? user.getId() : null;
    }

    /**
     * Obtiene el rol del usuario actual
     * 
     * @return Rol del usuario o null
     */
    public static Role getCurrentUserRole() {
        User user = userContext.get();
        return user != null ? user.getRole() : null;
    }

    /**
     * Obtiene el username del usuario actual
     * 
     * @return Username del usuario o null
     */
    public static String getCurrentUsername() {
        User user = userContext.get();
        return user != null ? user.getUsername() : null;
    }

    /**
     * Obtiene el document del usuario actual
     * 
     * @return Documento de identificación del usuario o null
     */
    public static String getCurrentUserDocument() {
        User user = userContext.get();
        return user != null ? user.getIdentificationId() : null;
    }

    /**
     * Verifica si hay un usuario autenticado en el contexto
     * 
     * @return true si hay usuario, false en caso contrario
     */
    public static boolean hasUser() {
        return userContext.get() != null;
    }

    /**
     * Limpia el contexto de seguridad
     * IMPORTANTE: Llamar al final de cada solicitud para evitar memory leaks
     */
    public static void clear() {
        userContext.remove();
    }

    /**
     * Crea un usuario anónimo desde los datos del JWT
     * Útil para operaciones que no requieren autenticación completa
     * 
     * @param username Username del token JWT
     * @param role Rol del token JWT (como String)
     * @param document Documento de identificación del token JWT
     * @return Usuario con datos básicos del JWT
     */
    public static User createUserFromJwt(String username, String role, String document) {
        User user = new User();
        user.setUsername(username);
        user.setIdentificationId(document);
        try {
            user.setRole(Role.valueOf(role));
        } catch (IllegalArgumentException e) {
            user.setRole(null);
        }
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }
}
