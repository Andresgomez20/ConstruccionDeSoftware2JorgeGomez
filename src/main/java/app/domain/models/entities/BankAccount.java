package app.domain.models.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Currency;

import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.vo.Money;

@Setter
@Getter
@NoArgsConstructor
public class BankAccount {

    private String accountNumber;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private String titularId;
    private Money currentBalance;
    private Currency currency;
    private LocalDate openingDate;

}