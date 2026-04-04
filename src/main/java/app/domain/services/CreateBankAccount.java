package app.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.ports.BankAccountPort;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class CreateBankAccount {

    private final BankAccountPort bankAccountPort;
    private final LogOperation logOperation; // 1. Inyectamos la bitácora

    @Autowired
    public CreateBankAccount(BankAccountPort bankAccountPort, LogOperation logOperation) {
        this.bankAccountPort = bankAccountPort;
        this.logOperation = logOperation;
    }

    public void createBankAccount(BankAccount bankAccount) throws BusinessException {
        // Regla de negocio: El número de cuenta debe ser único
        if (bankAccountPort.existsByAccountNumber(bankAccount.getAccountNumber())) {
            throw new BusinessException("Ya existe una cuenta bancaria con este número");
        }

        // Asignar fecha de apertura por defecto si no viene
        if (bankAccount.getOpeningDate() == null) {
            bankAccount.setOpeningDate(LocalDate.now());
        }

        // Guardar en MySQL
        bankAccountPort.save(bankAccount);

        // REGISTRO EN BITÁCORA
        Map<String, Object> details = new HashMap<>();
        details.put("accountType", bankAccount.getAccountType());
        details.put("initialBalance", bankAccount.getCurrentBalance());
        details.put("currency", "COP"); // O la que maneje tu modelo
        details.put("ownerId", bankAccount.getTitularId());

        logOperation.record(
            0L, // ID 0 o System, ya que suele ser un proceso administrativo o de apertura inicial
            "SYSTEM_ADMIN", 
            "BANK_ACCOUNT_OPENED", 
            bankAccount.getAccountNumber(), 
            details
        );
    }
}