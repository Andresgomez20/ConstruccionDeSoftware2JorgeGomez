package app.infrastructure.adapters.persistence.sql.entities;

import app.domain.models.enums.TransferStatus;
import app.domain.models.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
@Getter
@Setter
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceAccountNumber;
    private String destinationAccountNumber;

    // Descomponemos el VO Money
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private LocalDateTime timestamp;
}