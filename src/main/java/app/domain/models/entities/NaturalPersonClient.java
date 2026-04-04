package app.domain.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class NaturalPersonClient {

    private String identificationNumber; // Cédula u otro identificador (Único)
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    
}