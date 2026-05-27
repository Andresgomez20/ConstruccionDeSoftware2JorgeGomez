package app.infrastructure.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "natural_person_clients")
public class NaturalPersonClientEntity {

    @Id // Usamos la cédula como llave primaria
    @Column(unique = true, nullable = false)
    private String identificationNumber; 
    
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    
    public NaturalPersonClientEntity() {}
    
    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}