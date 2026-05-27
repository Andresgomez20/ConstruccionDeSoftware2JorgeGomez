package app.infrastructure.adapters.persistence.sql.entities;

import app.domain.models.enums.Role;
import app.domain.models.enums.UserStatus;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String relatedEntityId;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(unique = true, nullable = false)
    private String identificationId;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = true)
    private String phone;
    
    @Column(nullable = true)
    private LocalDate birthDate;
    
    @Column(nullable = true)
    private String address;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String status;
    
    public UserEntity() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getIdentificationId() { return identificationId; }
    public void setIdentificationId(String identificationId) { this.identificationId = identificationId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // GETTERS Y SETTERS 
    public Role getRole() { 
        if (this.role == null) return null;
        return Role.valueOf(this.role); 
    }
    
    public void setRole(Role role) { 
        if (role != null) {
            this.role = role.name(); 
        } else {
            this.role = null;
        }
    }
    
    public UserStatus getStatus() { 
        if (this.status == null) return null;
        return UserStatus.valueOf(this.status); 
    }
    
    public void setStatus(UserStatus status) { 
        if (status != null) {
            this.status = status.name(); 
        } else {
            this.status = null;
        }
    }
}