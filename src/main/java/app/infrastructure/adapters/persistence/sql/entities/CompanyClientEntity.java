package app.infrastructure.adapters.persistence.sql.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "company_clients")
public class CompanyClientEntity {

    @Id // NIT como llave primaria
    @Column(unique = true, nullable = false)
    private String taxIdentificationNumber; 
    
    private String businessName;
    private String email;
    private String phone;
    private String address;
    private String legalRepresentativeId;
    
    public CompanyClientEntity() {}
    
    public String getTaxIdentificationNumber() { return taxIdentificationNumber; }
    public void setTaxIdentificationNumber(String taxIdentificationNumber) { this.taxIdentificationNumber = taxIdentificationNumber; }
    
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getLegalRepresentativeId() { return legalRepresentativeId; }
    public void setLegalRepresentativeId(String legalRepresentativeId) { this.legalRepresentativeId = legalRepresentativeId; }
}