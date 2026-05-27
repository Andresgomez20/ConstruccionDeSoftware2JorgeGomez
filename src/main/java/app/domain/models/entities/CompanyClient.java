package app.domain.models.entities;

public class CompanyClient {

    private String taxIdentificationNumber; // NIT
    private String businessName;            // Razón Social
    private String email;
    private String phone;
    private String address;
    private String legalRepresentativeId;   // Referencia al identificationNumber de un NaturalPersonClient
    
    public CompanyClient() {}
    
    public CompanyClient(String taxIdentificationNumber, String businessName, String email, String phone, String address, String legalRepresentativeId) {
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.businessName = businessName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.legalRepresentativeId = legalRepresentativeId;
    }
    
    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }
    
    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }
    
    public String getBusinessName() {
        return businessName;
    }
    
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getLegalRepresentativeId() {
        return legalRepresentativeId;
    }
    
    public void setLegalRepresentativeId(String legalRepresentativeId) {
        this.legalRepresentativeId = legalRepresentativeId;
    }
    
}