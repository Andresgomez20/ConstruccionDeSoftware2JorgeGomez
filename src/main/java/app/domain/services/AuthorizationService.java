package app.domain.services;

import org.springframework.stereotype.Service;
import app.domain.Exceptions.BusinessException;
import app.domain.models.enums.Role;
import app.domain.models.identity.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//Servicio de Autorización que valida permisos según el rol del usuario

@Service
public class AuthorizationService {

    //Valida que el usuario tenga uno de los roles requeridos
   
    public void validateUserRole(User user, Role... requiredRoles) throws BusinessException {
        if (user == null) {
            throw new BusinessException("Usuario no autenticado.");
        }

        if (user.getRole() == null) {
            throw new BusinessException("El usuario no tiene rol asignado.");
        }

        Set<Role> allowed = new HashSet<>(Arrays.asList(requiredRoles));
        if (!allowed.contains(user.getRole())) {
            throw new BusinessException(
                "Acceso denegado. El rol '" + user.getRole().name() + 
                "' no tiene permisos para realizar esta operación. Roles permitidos: " + 
                Arrays.toString(requiredRoles)
            );
        }
    }

    //Valida que el usuario sea un INTERNAL_ANALYST (quien puede aprobar/rechazar préstamos)

    public void validateLoanApprovalPermission(User user) throws BusinessException {
        validateUserRole(user, Role.INTERNAL_ANALYST);
    }

    //Valida que el usuario sea un COMPANY_SUPERVISOR (quien puede aprobar transferencias de empresas)
    public void validateTransferApprovalPermission(User user) throws BusinessException {
        validateUserRole(user, Role.COMPANY_SUPERVISOR);
    }

    //Valida que el usuario sea empleado de ventanilla (puede crear cuentas, registros básicos)
    public void validateTellerPermission(User user) throws BusinessException {
        validateUserRole(user, Role.TELLER);
    }

    //Valida que el usuario sea comercial (puede crear préstamos)
    public void validateCommercialPermission(User user) throws BusinessException {
        validateUserRole(user, Role.COMMERCIAL);
    }

    //Valida que el usuario sea cliente empresa
    public void validateCompanyClientPermission(User user) throws BusinessException {
        validateUserRole(user, Role.CLIENT_COMPANY);
    }

    //Valida que el usuario sea cliente persona natural
    public void validateNaturalPersonClientPermission(User user) throws BusinessException {
        validateUserRole(user, Role.CLIENT_NATURAL_PERSON);
    }

    //Valida que el usuario sea operador de empresa
    public void validateCompanyOperatorPermission(User user) throws BusinessException {
        validateUserRole(user, Role.COMPANY_OPERATOR);
    }

    //Valida que el usuario sea analista interno
    public void validateInternalAnalystPermission(User user) throws BusinessException {
        validateUserRole(user, Role.INTERNAL_ANALYST);
    }

    //Verifica si un usuario tiene un rol específico
    public boolean hasRole(User user, Role role) {
        return user != null && user.getRole() == role;
    }

    //Verifica si un usuario tiene alguno de los roles especificados
    public boolean hasAnyRole(User user, Role... roles) {
        if (user == null || user.getRole() == null) {
            return false;
        }
        
        for (Role role : roles) {
            if (user.getRole() == role) {
                return true;
            }
        }
        return false;
    }
}
