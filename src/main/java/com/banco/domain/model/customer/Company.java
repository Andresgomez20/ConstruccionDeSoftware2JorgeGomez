package com.banco.domain.model.customer;

import java.util.UUID;
import com.banco.domain.model.vo.Email;


 // Representa a un cliente de tipo Persona Jurídica (Empresa).

public class Company extends Customer {
    private final String nit;

    public Company(UUID id, String name, Email email, String nit) {
        // Llamada al constructor de la clase padre (Customer)
        super(id, name, email);
        this.nit = nit;
    }

    public String getNit() {
        return nit;
    }
}