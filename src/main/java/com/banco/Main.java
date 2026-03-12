package com.banco;

import com.banco.domain.model.customer.NaturalPerson;
import com.banco.domain.model.account.Account;
import com.banco.domain.model.loan.Loan;
import com.banco.domain.model.loan.UserRole;
import com.banco.domain.model.vo.Email;
import com.banco.domain.model.vo.Money;
import com.banco.domain.exceptions.DomainException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- INICIANDO PRUEBAS DEL MODELO BANCARIO ---");

        try {
            // PRUEBA: Validación de Edad
            System.out.println("\n[Prueba 1] Creando cliente menor de edad...");
            new NaturalPerson(UUID.randomUUID(), "Juanito", new Email("juan@mail.com"), "123", LocalDate.now().minusYears(10));
        } catch (DomainException e) {
            System.err.println("Bloqueado: " + e.getMessage());
        }

        try {
            // PRUEBA: Saldo Insuficiente
            System.out.println("\n[Prueba 2] Intentando retirar más de lo que hay...");
            Account cuenta = new Account("789", UUID.randomUUID(), new Money(new BigDecimal("100"), "COP"));
            cuenta.withdraw(new Money(new BigDecimal("500"), "COP"));
        } catch (DomainException e) {
            System.err.println("Bloqueado: " + e.getMessage());
        }

        try {
            // PRUEBA: Seguridad por Roles
            System.out.println("\n[Prueba 3] Cliente intentando aprobar su propio préstamo...");
            Loan prestamo = new Loan(UUID.randomUUID(), UUID.randomUUID(), new Money(new BigDecimal("1000"), "COP"));
            prestamo.approve(UserRole.CLIENTE); // Debería fallar
        } catch (DomainException e) {
            System.err.println("Bloqueado: " + e.getMessage());
        }

        System.out.println("\n--- PRUEBAS FINALIZADAS ---");
    }
}