package com.banco.domain.model.customer;

import java.util.UUID;
import java.time.LocalDate;
import java.time.Period;
import com.banco.domain.model.vo.Email;
import com.banco.domain.exceptions.DomainException;


 // Representa a un cliente de tipo Persona Natural.
 // Incluye la regla de negocio de mayoría de edad (18 años).

public class NaturalPerson extends Customer {
    private final String idNumber;
    private final LocalDate birthDate;

    public NaturalPerson(UUID id, String name, Email email, String idNumber, LocalDate birthDate) {
        super(id, name, email);

        // Validación de Regla de Negocio: Mayoría de edad
        if (birthDate == null) {
            throw new DomainException("La fecha de nacimiento no puede ser nula.");
        }

        if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
            throw new DomainException("El cliente debe ser mayor de 18 años para abrir una cuenta.");
        }

        this.idNumber = idNumber;
        this.birthDate = birthDate;
    }

    // Getters específicos de Persona Natural
    public String getIdNumber() {
        return idNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}