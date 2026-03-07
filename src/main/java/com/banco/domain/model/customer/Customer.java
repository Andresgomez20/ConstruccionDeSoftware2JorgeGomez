package com.banco.domain.model.customer;

import com.banco.domain.exceptions.DomainException;
import java.util.UUID;

public abstract class customer {
    private final string idNumber;
    private final LocalDate birthDate;

    public NaturalPerson(UUID id, String name, Emial email, String idNumber, LocalDate birthDate) {
        super(id, name, email);

        // Regla de Negocio: Validación de mayoría de edad
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
            throw new DomainException("El cliente debe ser mayor de edad para abrir una cuenta");
        }

        this.idNumber = idNumber;
        this.birthDate = birthDate;
    }
    public String getIdNumber() {
        return idNumber;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
}