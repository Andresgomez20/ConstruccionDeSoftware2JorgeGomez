package com.banco.domain.model.vo;

import java.math.BigDecimal;
import com.banco.domain.exceptions.DomainException;

public record Money(BigDecimal amount, String currency) {
    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("El monto no puede ser negativo");
        }
        if (currency == null || currency.isBlank()) {
            throw new DomainException("La moneda es obligatoria");
        }
    }

    // Método para sumar dinero (Regla de negocio: misma moneda)
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new DomainException("No se pueden sumar monedas distintas");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
}