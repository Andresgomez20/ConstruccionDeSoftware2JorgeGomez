package app.domain.models.identity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public abstract class Person {
    
    private String document; // Cédula o NIT
    private String fullName; // Nombre completo o Razón Social
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate; // Fecha de nacimiento o constitución
    
}