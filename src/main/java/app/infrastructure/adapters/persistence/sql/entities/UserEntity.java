package app.infrastructure.adapters.persistence.sql.entities;

import app.domain.models.enums.Role;
import app.domain.models.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String relatedEntityId;
    private String fullName;
    
    @Column(unique = true, nullable = false)
    private String identificationId;
    
    @Column(unique = true)
    private String email;
    
    private String phone;
    private LocalDate birthDate;
    private String address;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;
}