package app.domain.models.vo;

import app.domain.models.enums.Currency;
import app.domain.Exceptions.BusinessException;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new BusinessException("El monto no puede ser nulo.");
        }
        this.amount = amount;
        this.currency = currency;
    }

    // Método para sumar dinero asegurando que sea la misma moneda
    public Money add(Money other) {
        checkCurrency(other);
        return new Money(this.amount.add(other.getAmount()), this.currency);
    }

    public Money subtract(Money other) {
        checkCurrency(other);
        if (this.amount.compareTo(other.getAmount()) < 0) {
            throw new BusinessException("Fondos insuficientes para la operación.");
        }
        return new Money(this.amount.subtract(other.getAmount()), this.currency);
    }

    private void checkCurrency(Money other) {
        if (this.currency != other.getCurrency()) {
            throw new BusinessException("No se pueden realizar operaciones entre diferentes monedas: " 
                + this.currency + " y " + other.getCurrency());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    public int compareTo(Money other) {
        checkCurrency(other); // Reutilizamos tu validación para no comparar Peras con Manzanas (COP vs USD)
        return this.amount.compareTo(other.getAmount());
    }
}