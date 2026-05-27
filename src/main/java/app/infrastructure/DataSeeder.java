package app.infrastructure;

import app.domain.Exceptions.BusinessException;
import app.domain.models.entities.BankAccount;
import app.domain.models.entities.CompanyClient;
import app.domain.models.entities.Loan;
import app.domain.models.entities.NaturalPersonClient;
import app.domain.models.entities.Transfer;
import app.domain.models.enums.AccountStatus;
import app.domain.models.enums.AccountType;
import app.domain.models.enums.Currency;
import app.domain.models.enums.LoanStatus;
import app.domain.models.enums.Role;
import app.domain.models.enums.TransferStatus;
import app.domain.models.enums.UserStatus;
import app.domain.models.identity.User;
import app.domain.models.vo.Money;
import app.domain.services.CreateBankAccount;
import app.domain.services.CreateTransfer;
import app.domain.services.CreateUser;
import app.domain.services.RegisterCompany;
import app.domain.services.RegisterNaturalPerson;
import app.domain.services.RequestLoan;
import app.infrastructure.adapters.persistence.sql.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataSeeder para el Sistema Bancario
 * Crea datos de prueba al iniciar la aplicación:
 * - 1 Usuario por cada Role (7 usuarios) con password "12345"
 * - Clientes Personas Naturales (5)
 * - Clientes Empresariales (3)
 * - Cuentas Bancarias (8 total)
 * - Transferencias (4)
 * - Préstamos (3)
 */
@Component
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CreateUser createUser;
    private final RegisterNaturalPerson registerNaturalPerson;
    private final RegisterCompany registerCompany;
    private final CreateBankAccount createBankAccount;
    private final CreateTransfer createTransfer;
    private final RequestLoan requestLoan;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      CreateUser createUser,
                      RegisterNaturalPerson registerNaturalPerson,
                      RegisterCompany registerCompany,
                      CreateBankAccount createBankAccount,
                      CreateTransfer createTransfer,
                      RequestLoan requestLoan,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.createUser = createUser;
        this.registerNaturalPerson = registerNaturalPerson;
        this.registerCompany = registerCompany;
        this.createBankAccount = createBankAccount;
        this.createTransfer = createTransfer;
        this.requestLoan = requestLoan;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Solo ejecutar si la BD está vacía
        if (userRepository.count() > 0) {
            return;
        }

        System.out.println("===== INICIANDO CARGA DE DATOS DE PRUEBA =====");
        
        List<User> users = seedUsers();
        List<NaturalPersonClient> naturalClients = seedNaturalPersonClients();
        List<CompanyClient> companyClients = seedCompanyClients();
        List<BankAccount> bankAccounts = seedBankAccounts(naturalClients, companyClients, users);
        
        seedTransfers(bankAccounts, users);
        seedLoans(naturalClients, companyClients, bankAccounts);

        System.out.println("===== CARGA DE DATOS COMPLETADA =====");
    }

    // =========================================================================
    // 1. USUARIOS - Un usuario por cada Role con password "12345"
    // =========================================================================

    private List<User> seedUsers() throws BusinessException {
        String encodedPassword = passwordEncoder.encode("12345");
        LocalDate birthDate = LocalDate.of(1990, 1, 1);

        Map<Role, String> roleData = new HashMap<>();
        roleData.put(Role.CLIENT_NATURAL_PERSON, "Cliente Persona Natural");
        roleData.put(Role.CLIENT_COMPANY, "Cliente Empresa");
        roleData.put(Role.TELLER, "Empleado Ventanilla");
        roleData.put(Role.COMMERCIAL, "Empleado Comercial");
        roleData.put(Role.COMPANY_OPERATOR, "Operador Empresa");
        roleData.put(Role.COMPANY_SUPERVISOR, "Supervisor Empresa");
        roleData.put(Role.INTERNAL_ANALYST, "Analista Interno");

        List<User> users = new ArrayList<>();
        int counter = 1;

        for (Role role : roleData.keySet()) {
            String document = "1000000" + String.format("%02d", counter);
            String username = role.name().toLowerCase().replace("_", "");
            String email = username + "@banco.com";

            User user = buildUser(
                document,
                roleData.get(role),
                username,
                encodedPassword,
                role,
                email,
                "300" + String.format("%08d", counter),
                "Calle " + counter + " # 1-1",
                birthDate.plusYears(counter)
            );

            createUser.execute(user);
            users.add(user);

            System.out.println("✓ Usuario creado: " + username + " (" + role.name() + ")");
            counter++;
        }

        return users;
    }

    private User buildUser(String document, String fullName, String username, String password,
                           Role role, String email, String phone, String address, LocalDate birthDate) {
        User user = new User();
        user.setIdentificationId(document);
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setBirthDate(birthDate);
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }

    // =========================================================================
    // 2. CLIENTES PERSONAS NATURALES
    // =========================================================================

    private List<NaturalPersonClient> seedNaturalPersonClients() {
        String[][] clientData = {
            {"1100000001", "Carlos Gómez Pérez", "3101111001", "carlos@mail.com", "Carrera 10 # 20-30", "1990-05-15"},
            {"1100000002", "María Rodríguez López", "3101111002", "maria@mail.com", "Calle 45 # 12-10", "1985-08-22"},
            {"1100000003", "Luis Martínez García", "3101111003", "luis@mail.com", "Avenida 30 # 5-60", "1978-11-03"},
            {"1100000004", "Ana Torres Sánchez", "3101111004", "ana@mail.com", "Calle 80 # 40-20", "1995-02-28"},
            {"1100000005", "Jorge Herrera Ramírez", "3101111005", "jorge@mail.com", "Carrera 50 # 10-15", "2000-07-10"},
        };

        List<NaturalPersonClient> clients = new ArrayList<>();
        for (String[] data : clientData) {
            NaturalPersonClient client = new NaturalPersonClient();
            client.setIdentificationNumber(data[0]);
            client.setFullName(data[1]);
            client.setPhone(data[2]);
            client.setEmail(data[3]);
            client.setAddress(data[4]);
            client.setBirthDate(LocalDate.parse(data[5]));

            registerNaturalPerson.execute(client);
            clients.add(client);

            System.out.println("✓ Cliente Persona Natural creado: " + client.getFullName());
        }

        return clients;
    }

    // =========================================================================
    // 3. CLIENTES EMPRESARIALES
    // =========================================================================

    private List<CompanyClient> seedCompanyClients() {
        String[][] companyData = {
            {"860123456789", "Constructora TechBuild S.A.S", "3201111001", "info@techbuild.co", "Avenida Paseo Bolívar # 45-67", "1100000001"},
            {"860098765432", "Distribuidora LogísticaMax S.A.S", "3201111002", "info@logisticamax.co", "Carrera 9 # 100-50", "1100000002"},
            {"860111222333", "Consultoría Empresarial Andes S.A.S", "3201111003", "info@andesconsultoria.co", "Calle 26 # 15-40", "1100000003"},
        };

        List<CompanyClient> companies = new ArrayList<>();
        for (String[] data : companyData) {
            CompanyClient company = new CompanyClient();
            company.setTaxIdentificationNumber(data[0]);
            company.setBusinessName(data[1]);
            company.setPhone(data[2]);
            company.setEmail(data[3]);
            company.setAddress(data[4]);
            company.setLegalRepresentativeId(data[5]);

            registerCompany.execute(company);
            companies.add(company);

            System.out.println("✓ Cliente Empresarial creado: " + company.getBusinessName());
        }

        return companies;
    }

    // =========================================================================
    // 4. CUENTAS BANCARIAS
    // =========================================================================

    private List<BankAccount> seedBankAccounts(List<NaturalPersonClient> naturalClients,
                                                List<CompanyClient> companyClients,
                                                List<User> users) throws BusinessException {
        List<BankAccount> accounts = new ArrayList<>();
        int accountNumber = 4000001;

        // 5 cuentas para clientes personas naturales
        for (int i = 0; i < naturalClients.size(); i++) {
            BankAccount account = new BankAccount();
            account.setAccountNumber(String.valueOf(accountNumber++));
            account.setAccountType(i % 2 == 0 ? AccountType.CHECKING : AccountType.SAVINGS);
            account.setAccountStatus(AccountStatus.ACTIVE);
            account.setTitularId(naturalClients.get(i).getIdentificationNumber());
            account.setCurrentBalance(new Money(
                new BigDecimal("50000").add(new BigDecimal(i * 10000)),
                Currency.COP
            ));
            account.setOpeningDate(LocalDate.now().minusMonths(i + 1));

            createBankAccount.execute(account);
            accounts.add(account);

            System.out.println("✓ Cuenta creada para " + naturalClients.get(i).getFullName() 
                + " - Número: " + account.getAccountNumber());
        }

        // 3 cuentas para clientes empresariales
        for (int i = 0; i < companyClients.size(); i++) {
            BankAccount account = new BankAccount();
            account.setAccountNumber(String.valueOf(accountNumber++));
            account.setAccountType(AccountType.CHECKING); // Las empresas usan principalmente cuentas corrientes
            account.setAccountStatus(AccountStatus.ACTIVE);
            account.setTitularId(companyClients.get(i).getTaxIdentificationNumber());
            account.setCurrentBalance(new Money(
                new BigDecimal("500000").add(new BigDecimal(i * 100000)),
                Currency.COP
            ));
            account.setOpeningDate(LocalDate.now().minusMonths(i + 3));

            createBankAccount.execute(account);
            accounts.add(account);

            System.out.println("✓ Cuenta creada para " + companyClients.get(i).getBusinessName() 
                + " - Número: " + account.getAccountNumber());
        }

        return accounts;
    }

    // =========================================================================
    // 5. TRANSFERENCIAS
    // =========================================================================

    private void seedTransfers(List<BankAccount> bankAccounts, List<User> users) {
        if (bankAccounts.size() < 2) {
            return;
        }

        User teller = users.stream()
            .filter(u -> u.getRole() == Role.TELLER)
            .findFirst()
            .orElse(users.get(0));

        // Transferencia 1: Cuenta 0 -> Cuenta 1
        Transfer transfer1 = new Transfer();
        transfer1.setOriginAccount(bankAccounts.get(0).getAccountNumber());
        transfer1.setDestinationAccount(bankAccounts.get(1).getAccountNumber());
        transfer1.setAmount(new Money(new BigDecimal("10000"), Currency.COP));
        transfer1.setCreationDate(LocalDateTime.now().minusDays(5));
        transfer1.setApprovalDate(LocalDateTime.now().minusDays(5));
        transfer1.setStatus(TransferStatus.EXECUTED);
        transfer1.setCreatorUserId(teller.getId());
        transfer1.setApproverUserId(teller.getId());

        try {
            // Establecer el usuario en SecurityContext para que CreateTransfer lo obtenga
            app.infrastructure.security.SecurityContext.setCurrentUser(teller);
            createTransfer.execute(transfer1);
            System.out.println("✓ Transferencia 1 creada (EXECUTED)");
        } catch (Exception e) {
            System.out.println("✗ Error creando transferencia 1: " + e.getMessage());
        } finally {
            app.infrastructure.security.SecurityContext.clear();
        }

        // Transferencia 2: Cuenta 1 -> Cuenta 2
        if (bankAccounts.size() > 2) {
            Transfer transfer2 = new Transfer();
            transfer2.setOriginAccount(bankAccounts.get(1).getAccountNumber());
            transfer2.setDestinationAccount(bankAccounts.get(2).getAccountNumber());
            transfer2.setAmount(new Money(new BigDecimal("5000"), Currency.COP));
            transfer2.setCreationDate(LocalDateTime.now().minusDays(3));
            transfer2.setApprovalDate(null);
            transfer2.setStatus(TransferStatus.PENDING_APPROVAL);
            transfer2.setCreatorUserId(teller.getId());

            try {
                // Establecer el usuario en SecurityContext
                app.infrastructure.security.SecurityContext.setCurrentUser(teller);
                createTransfer.execute(transfer2);
                System.out.println("✓ Transferencia 2 creada (PENDING_APPROVAL)");
            } catch (Exception e) {
                System.out.println("✗ Error creando transferencia 2: " + e.getMessage());
            } finally {
                app.infrastructure.security.SecurityContext.clear();
            }
        }

        // Transferencia 3: Cuenta 5 -> Cuenta 6
        if (bankAccounts.size() > 6) {
            Transfer transfer3 = new Transfer();
            transfer3.setOriginAccount(bankAccounts.get(5).getAccountNumber());
            transfer3.setDestinationAccount(bankAccounts.get(6).getAccountNumber());
            transfer3.setAmount(new Money(new BigDecimal("25000"), Currency.COP));
            transfer3.setCreationDate(LocalDateTime.now().minusDays(1));
            transfer3.setApprovalDate(LocalDateTime.now().minusDays(1));
            transfer3.setStatus(TransferStatus.EXECUTED);
            transfer3.setCreatorUserId(users.get(4).getId()); // COMPANY_OPERATOR
            transfer3.setApproverUserId(users.get(4).getId());

            try {
                // Establecer usuario empresa/comercial en SecurityContext
                app.infrastructure.security.SecurityContext.setCurrentUser(users.get(4));
                createTransfer.execute(transfer3);
                System.out.println("✓ Transferencia 3 creada (EXECUTED - Empresa)");
            } catch (Exception e) {
                System.out.println("✗ Error creando transferencia 3: " + e.getMessage());
            } finally {
                app.infrastructure.security.SecurityContext.clear();
            }
        }

        // Transferencia 4: Cuenta 2 -> Cuenta 3
        if (bankAccounts.size() > 3) {
            Transfer transfer4 = new Transfer();
            transfer4.setOriginAccount(bankAccounts.get(2).getAccountNumber());
            transfer4.setDestinationAccount(bankAccounts.get(3).getAccountNumber());
            transfer4.setAmount(new Money(new BigDecimal("15000"), Currency.COP));
            transfer4.setCreationDate(LocalDateTime.now());
            transfer4.setStatus(TransferStatus.PENDING_APPROVAL);
            transfer4.setCreatorUserId(teller.getId());

            try {
                // Establecer el usuario en SecurityContext
                app.infrastructure.security.SecurityContext.setCurrentUser(teller);
                createTransfer.execute(transfer4);
                System.out.println("✓ Transferencia 4 creada (PENDING_APPROVAL)");
            } catch (Exception e) {
                System.out.println("✗ Error creando transferencia 4: " + e.getMessage());
            } finally {
                app.infrastructure.security.SecurityContext.clear();
            }
        }
    }

    // =========================================================================
    // 6. PRÉSTAMOS
    // =========================================================================

    private void seedLoans(List<NaturalPersonClient> naturalClients,
                           List<CompanyClient> companyClients,
                           List<BankAccount> bankAccounts) {
        if (naturalClients.isEmpty() || bankAccounts.isEmpty()) {
            return;
        }

        // Préstamo 1: Cliente persona natural - UNDER_REVIEW
        Loan loan1 = new Loan();
        loan1.setClientDocument(naturalClients.get(0).getIdentificationNumber());
        loan1.setLoanType("Crédito Personal");
        loan1.setRequestedAmount(new Money(new BigDecimal("500000"), Currency.COP));
        loan1.setInterestRate(new BigDecimal("1.5")); // 1.5% mensual
        loan1.setTermMonths(12);
        loan1.setDestinationAccount(bankAccounts.get(0).getAccountNumber());
        loan1.setStatus(LoanStatus.UNDER_REVIEW);

        try {
            requestLoan.request(loan1);
            System.out.println("✓ Préstamo 1 creado (UNDER_REVIEW) - Cliente: " + naturalClients.get(0).getFullName());
        } catch (Exception e) {
            System.out.println("✗ Error creando préstamo 1: " + e.getMessage());
        }

        // Préstamo 2: Cliente persona natural - APPROVED
        if (naturalClients.size() > 1 && bankAccounts.size() > 1) {
            Loan loan2 = new Loan();
            loan2.setClientDocument(naturalClients.get(1).getIdentificationNumber());
            loan2.setLoanType("Crédito para Vivienda");
            loan2.setRequestedAmount(new Money(new BigDecimal("100000000"), Currency.COP));
            loan2.setApprovedAmount(new Money(new BigDecimal("80000000"), Currency.COP));
            loan2.setInterestRate(new BigDecimal("0.8")); // 0.8% mensual
            loan2.setTermMonths(240); // 20 años
            loan2.setApprovalDate(LocalDate.now().minusDays(10));
            loan2.setDestinationAccount(bankAccounts.get(1).getAccountNumber());
            loan2.setStatus(LoanStatus.APPROVED);

            try {
                requestLoan.request(loan2);
                System.out.println("✓ Préstamo 2 creado (APPROVED) - Cliente: " + naturalClients.get(1).getFullName());
            } catch (Exception e) {
                System.out.println("✗ Error creando préstamo 2: " + e.getMessage());
            }
        }

        // Préstamo 3: Cliente empresarial - DISBURSED
        if (!companyClients.isEmpty() && bankAccounts.size() > 5) {
            Loan loan3 = new Loan();
            loan3.setClientDocument(companyClients.get(0).getTaxIdentificationNumber());
            loan3.setLoanType("Línea de Crédito Empresarial");
            loan3.setRequestedAmount(new Money(new BigDecimal("50000000"), Currency.COP));
            loan3.setApprovedAmount(new Money(new BigDecimal("50000000"), Currency.COP));
            loan3.setInterestRate(new BigDecimal("1.2")); // 1.2% mensual
            loan3.setTermMonths(36); // 3 años
            loan3.setApprovalDate(LocalDate.now().minusDays(20));
            loan3.setDisbursementDate(LocalDate.now().minusDays(15));
            loan3.setDestinationAccount(bankAccounts.get(5).getAccountNumber());
            loan3.setStatus(LoanStatus.DISBURSED);

            try {
                requestLoan.request(loan3);
                System.out.println("✓ Préstamo 3 creado (DISBURSED) - Cliente: " + companyClients.get(0).getBusinessName());
            } catch (Exception e) {
                System.out.println("✗ Error creando préstamo 3: " + e.getMessage());
            }
        }
    }
}
