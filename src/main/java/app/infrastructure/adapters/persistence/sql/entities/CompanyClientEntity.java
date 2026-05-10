package app.infrastructure.adapters.persistence.sql.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "company_clients")
@Getter
@Setter
public class CompanyClientEntity {

    @Id // NIT como llave primaria
    @Column(unique = true, nullable = false)
    private String taxIdentificationNumber; 
    
    private String businessName;
    private String email;
    private String phone;
    private String address;
    private String legalRepresentativeId;
}