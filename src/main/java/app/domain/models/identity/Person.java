package app.domain.models.identity;

import java.time.LocalDate;

public abstract class Person {
    
    private String document; // Cédula o NIT
    private String fullName; // Nombre completo o Razón Social
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate; // Fecha de nacimiento o constitución
    
    public Person() {}
    
    public Person(String document, String fullName, String email, String phone, String address, LocalDate birthDate) {
        this.document = document;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.birthDate = birthDate;
    }
    
    public String getDocument() {
        return document;
    }
    
    public void setDocument(String document) {
        this.document = document;
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
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
}