package app.domain.models.vo;

import app.domain.models.enums.Currency;
import app.domain.Exceptions.BusinessException;
import com.fasterxml.jackson.annotation.JsonCreator; 
import com.fasterxml.jackson.annotation.JsonProperty; 
import lombok.Getter;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    // Le decimos a Spring Boot cómo construir este objeto desde el JSON
    @JsonCreator 
    public Money(
            @JsonProperty("amount") BigDecimal amount, 
            @JsonProperty("currency") Currency currency
    ) {
        if (amount == null) {
            throw new BusinessException("El monto no puede ser nulo.");
        }
        if (currency == null) {
            throw new BusinessException("La moneda no puede ser nula.");
        }
        this.amount = amount;
        this.currency = currency;
    }

    
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
        checkCurrency(other); 
        return this.amount.compareTo(other.getAmount());
    }
}