package com.banco.domain.model.vo;

import com.banco.domain.exceptions.DomainException;

public record Email(String value) {
    public Email {
        if (value == null || !value.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new DomainException("El formato del correo electrónico es inválido");
        }
    }
}