package app.domain.models.identity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import app.domain.models.enums.Role;
import app.domain.models.enums.UserStatus;

@Setter
@Getter
@NoArgsConstructor
public class User {

    private Long id; // Identificador interno autogenerado
    
    // ID_Relacionado
    private String relatedEntityId; 
    
    private String fullName;
    private String identificationId; // Cédula o NIT del usuario que opera
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    
    // Datos de autenticación y sistema
    private String username;
    private String password;
    private Role role;
    private UserStatus status;

}