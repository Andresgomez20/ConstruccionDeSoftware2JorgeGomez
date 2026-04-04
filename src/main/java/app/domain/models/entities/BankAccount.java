package app.domain.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import app.domain.models.vo.Money;

@Setter
@Getter
@NoArgsConstructor
public class BankAccount {

    private String accountNumber;
    private String accountType; 
    private String titularId;
    private Money currentBalance;
    private String currency;
    private String accountStatus; 
    private LocalDate openingDate;

}