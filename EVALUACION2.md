# EVALUACION 2 - ConstruccionDeSoftware2JorgeGomez

## Informacion general
- Estudiante(s): Karen Ospina, Jorge Andres Gomez
- Rama evaluada: main
- Commit evaluado: 91b49d827588c2c10987f86397d07c6f92fb0d9b
- Fecha: 2026-04-11

## Tabla de calificacion

| Criterio | Peso | Puntaje (1-5) | Aporte |
|---|---|---|---|
| 1. Modelado de dominio | 20% | 4 | 0.80 |
| 2. Modelado de puertos | 20% | 5 | 1.00 |
| 3. Modelado de servicios de dominio | 20% | 4 | 0.80 |
| 4. Enums y estados | 10% | 3 | 0.30 |
| 5. Reglas de negocio criticas | 10% | 4 | 0.40 |
| 6. Bitacora y trazabilidad | 5% | 5 | 0.25 |
| 7. Estructura interna de dominio | 10% | 4 | 0.40 |
| 8. Calidad tecnica base en domain | 5% | 3 | 0.15 |
| **SUBTOTAL** | | | **4.10** |

## Penalizaciones
- **Acoplamiento del dominio a Spring (-25%):** Los servicios de dominio usan `@Service` y `@Transactional` de Spring.

Calculo: 4.10 x 0.75 = **3.08**

## Bonus
- +0.2: Puertos bien disenados, 7 puertos con firmas semanticas.
- +0.2: Servicio `Money` (Value Object) con alta cohesion y proteccion de invariantes monetarios.
- +0.1: Bitacora integrada a traves de `LogOperation` en todos los flujos transaccionales.

Total bonus: +0.5

## Nota final
**3.6 / 5.0**

---

## Hallazgos

### Positivos
- **Value Object `Money` excepcional:** usa `BigDecimal`, inmutable, metodos `add()`, `subtract()` con validacion de moneda cruzada, lanza excepcion por fondos insuficientes.
- **7 puertos semanticos:** `UserPort`, `BankAccountPort`, `LoanPort`, `TransferPort`, `OperationLogPort`, `CompanyClientPort`, `NaturalPersonClientPort`.
- **9 servicios de dominio:** `ApproveLoan`, `CreateTransfer`, `CreateUser`, `CreateBankAccount`, `LogOperation`, `RegisterCompany`, `RegisterNaturalPerson`, `RequestLoan`, `ApproveTransfer`.
- `CreateTransfer` valida: cuenta bloqueada/cancelada, umbral de aprobacion ($10,000), llama auditoria.
- `OperationLog` entity + `LogOperation` service para trazabilidad completa.
- `BusinessException` explicita.
- Estructura con subcarpetas `entities/`, `enums/`, `vo/`, `identity/`, `ports/`, `services/`.

### Negativos
- **`@Service` y `@Transactional` en servicios de dominio.** El dominio no debe importar `org.springframework.*`. Penaliza -25%.
- `accountType` y `accountStatus` en `BankAccount` son `String` en lugar de enum.
- Solo 5 enums cuando se requieren mas (falta `Currency` como enum propio de `Money`, `AccountType` como enum independiente).
- Faltan servicios para: desembolso de prestamos, rechazo de prestamos, expiracion de transferencias, bloqueo/cancelacion de cuentas.

## Recomendaciones
1. Eliminar `@Service` y `@Transactional` de todos los servicios en `domain/services/`. Usar inyeccion por constructor sin framework.
2. Reemplazar `accountType: String` y `accountStatus: String` por sus respectivos enums.
3. Agregar servicios faltantes del enunciado: `DisburseLoan`, `RejectLoan`, `ExpireTransfer`, `BlockBankAccount`, `CancelBankAccount`.
4. El `Money` VO es un ejemplo excelente de diseno; mantenerlo y extenderlo.
