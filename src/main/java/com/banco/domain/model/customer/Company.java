package com.banco.domain.model.customer;

import java.util.UUID;
import com.banco.domain.model.vo.Email;

public class Company extends Customer {
    private final String nit;

    public Company(UUID id, String name, Email email, String nit) {
        super(id, name, email);
        this.nit = nit;
    }

    public String getNit() {
        return nit;
    }
}