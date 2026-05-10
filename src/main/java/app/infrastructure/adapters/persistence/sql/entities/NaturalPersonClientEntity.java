package app.infrastructure.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "natural_person_clients")
@Getter
@Setter
public class NaturalPersonClientEntity {

    @Id // Usamos la cédula como llave primaria
    @Column(unique = true, nullable = false)
    private String identificationNumber; 
    
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
}