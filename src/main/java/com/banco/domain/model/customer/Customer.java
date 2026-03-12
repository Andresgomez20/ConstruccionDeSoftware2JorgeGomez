package com.banco.domain.model.customer;

import java.util.UUID;
import com.banco.domain.model.vo.Email;


 // Clase base para todos los clientes del banco.
 // Se define como abstracta porque no puede haber un cliente genérico.

public abstract class Customer {
    private final UUID id;
    private final String name;
    private final Email email;
    private boolean active;

    protected Customer(UUID id, String name, Email email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.active = true; // Por defecto todo cliente inicia activo
    }

    // Getters necesarios para que las clases hijas y el Main puedan ver la info
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}