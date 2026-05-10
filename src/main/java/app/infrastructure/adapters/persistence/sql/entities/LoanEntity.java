package app.infrastructure.adapters.persistence.sql.entities;

import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
public class LoanEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientDocument;

    // Guardamos el Money de monto solicitado
    private BigDecimal requestedAmount;
    
    @Enumerated(EnumType.STRING)
    private Currency requestedCurrency;

    // Guardamos el Money de monto aprobado
    private BigDecimal approvedAmount;
    
    @Enumerated(EnumType.STRING)
    private Currency approvedCurrency;

    private String destinationAccount;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    private LocalDate disbursementDate;
}