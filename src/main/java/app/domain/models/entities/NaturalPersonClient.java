package app.domain.models.entities;

import java.time.LocalDate;

public class NaturalPersonClient {

    private String identificationNumber; // Cédula u otro identificador (Único)
    private String fullName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    
    public NaturalPersonClient() {}
    
    public NaturalPersonClient(String identificationNumber, String fullName, String email, String phone, LocalDate birthDate, String address) {
        this.identificationNumber = identificationNumber;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
    }
    
    public String getIdentificationNumber() {
        return identificationNumber;
    }
    
    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
}