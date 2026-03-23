# 📋 EVALUACIÓN - Sistema Bancario
**Proyecto:** ConstruccionDeSoftware2JorgeGomez
**Estudiantes:** Karen Ospina, Jorge Andrés Gómez
**Fecha de evaluación:** 23/03/2026
**Nota final: 3.6 / 5.0**

---

## 📊 Tabla de Puntajes

| Criterio | Peso | Puntaje (1-5) | Contribución |
|----------|------|----------------|--------------|
| 1. Modelado de dominio | 25% | 3 | 0.75 |
| 2. Relaciones entre entidades | 15% | 3 | 0.45 |
| 3. Uso de enums | 15% | 3 | 0.45 |
| 4. Manejo de estados | 5% | 3 | 0.15 |
| 5. Tipos de datos | 5% | 5 | 0.25 |
| 6. Separación Usuario vs Cliente | 10% | 2 | 0.20 |
| 7. Bitácora | 5% | 1 | 0.05 |
| 8. Reglas básicas de negocio | 5% | 5 | 0.25 |
| 9. Estructura del proyecto | 10% | 4 | 0.40 |
| 10. Repositorio | 10% | 2 | 0.20 |
| **TOTAL BASE** | 100% | | **3.15** |

### Bonus Aplicados

| Bonus | Puntaje |
|-------|---------|
| Herencia correcta (NaturalPerson/Company extends Customer) | +0.20 |
| Código muy limpio y DDD-style con Value Objects | +0.20 |
| Nombres y estructura consistentes | +0.05 |
| **Total bonus** | **+0.45** |

### Penalizaciones Aplicadas
Ninguna.

**NOTA FINAL: 3.60 / 5.0**

---

## 🔍 Análisis Detallado por Criterio

### 1. Modelado de dominio → 3/5
**Entidades presentes:**
- ✅ `Customer` (abstract) — base para clientes
- ✅ `NaturalPerson` (extends Customer) — cliente persona natural con validación de edad
- ✅ `Company` (extends Customer) — cliente empresa
- ✅ `Account` — cuenta bancaria con lógica de dominio
- ✅ `Loan` — préstamo con reglas de aprobación y desembolso

**Entidades faltantes (críticas):**
- ❌ `User` — no existe entidad de usuario del sistema (solo el enum `UserRole`)
- ❌ `Transfer` — no hay modelo de transferencias
- ❌ `BankProduct` — no hay catálogo de productos bancarios
- ❌ `Bitácora` — no hay registro de operaciones

El proyecto muestra alta calidad en lo que implementa, pero está **incompleto en alcance**. Entidades fundamentales del dominio bancario no fueron desarrolladas.

### 2. Relaciones entre entidades → 3/5
- ✅ `NaturalPerson` y `Company` extienden `Customer` — herencia correcta
- ✅ `Account` tiene `ownerId` (UUID) — referencia al cliente
- ✅ `Loan` tiene `customerId` (UUID) y acepta un `Account` como destino de desembolso
- ✅ `Loan.disburse(Account)` genera relación funcional entre préstamo y cuenta
- ❌ No hay relación `Customer → List<Account>`
- ❌ No hay relaciones entre `Transfer` y las cuentas (Transfer no existe)

### 3. Uso de enums → 3/5
**Enums presentes:**
- ✅ `AccountStatus` — ACTIVA, INACTIVA, CERRADA
- ✅ `LoanStatus` — EN_ESTUDIO, APROBADO, RECHAZADO, DESEMBOLSADO
- ✅ `UserRole` — CLIENTE, ANALISTA_INTERNO, ADMINISTRADOR_SISTEMA

**Faltantes:**
- ❌ No hay `TransferStatus` (Transfer no implementada)
- ❌ No hay `UserStatus` para estado del usuario
- ❌ No hay `AccountType`, `Currency`, `ProductCategory`
- ❌ `Money.currency` es `String` — debería ser un enum `Currency`

### 4. Manejo de estados → 3/5
- ✅ `Account.status` usa `AccountStatus` enum correctamente
- ✅ `Loan.status` usa `LoanStatus` con transiciones correctas (EN_ESTUDIO → APROBADO → DESEMBOLSADO)
- ❌ No hay estados de Transferencia, Usuario ni Producto (no implementados)

### 5. Tipos de datos → 5/5
**Uso ejemplar de tipos de datos:**
- ✅ `Money` record con `BigDecimal amount` — excelente abstracción
- ✅ `Email` record con validación de formato regex
- ✅ `UUID` para identificadores únicos
- ✅ `LocalDate` para fechas de nacimiento
- Los Value Objects `Money` y `Email` son una práctica excelente para proteger invariantes

### 6. Separación Usuario vs Cliente → 2/5
- ✅ Existe la jerarquía `Customer` (NaturalPerson, Company) — concepto de cliente
- ❌ **No existe una entidad `User`** — el acceso al sistema no está modelado
- ⚠️ `UserRole` existe como enum pero sin una clase `User` que lo use
- ❌ No hay credenciales (username/password/status) en ninguna entidad
- La separación no puede evaluarse positivamente cuando uno de los conceptos (Usuario) simplemente no existe

### 7. Bitácora → 1/5
No existe ninguna entidad ni clase relacionada con bitácora o registro de operaciones.

### 8. Reglas básicas de negocio → 5/5
**El punto más destacable del proyecto.** Implementación ejemplar de reglas de negocio:

- ✅ `NaturalPerson` — **valida que el cliente sea mayor de 18 años** en el constructor
- ✅ `Account.deposit()` — verifica que la cuenta esté activa antes de depositar
- ✅ `Account.withdraw()` — verifica estado activo y **saldo suficiente**
- ✅ `Account.ensureActive()` — método privado que aplica la regla de cuenta activa
- ✅ `Loan` constructor — valida que `amount > 0`
- ✅ `Loan.approve(UserRole)` — **solo `ANALISTA_INTERNO` puede aprobar** — regla de rol
- ✅ `Loan.approve()` — valida estado `EN_ESTUDIO` antes de aprobar
- ✅ `Loan.disburse(Account)` — solo se desembolsa si está `APROBADO`
- ✅ `Email` record — valida formato de email con regex
- ✅ `Money` record — valida monto no negativo y moneda no nula
- ✅ `DomainException` para errores de dominio

**Este es el único proyecto del grupo que implementa lógica de dominio real.**

### 9. Estructura del proyecto → 4/5
Organización DDD clara:
```
com.banco.domain.exceptions/
com.banco.domain.model.account/
com.banco.domain.model.customer/
com.banco.domain.model.loan/
com.banco.domain.model.vo/          ← Value Objects
```
La separación por subdominios es una práctica avanzada. Se descuenta por ausencia de `User`, `Transfer` y `Bitacora`.

### 10. Repositorio → 2/5
- ✅ Repositorio existe con README
- ⚠️ README muy básico: solo lista los integrantes sin información de tecnología, materia, cómo ejecutar
- ❌ No hay información sobre commits, ramas, tag de entrega

---

## 🌟 Puntos Destacables

- **Mejor implementación de reglas de negocio del grupo** — lógica real en los modelos
- Uso de **Java Records** para Value Objects (`Email`, `Money`) — muy avanzado
- Validación de edad mayor de 18 años en creación de cliente
- Solo el analista interno puede aprobar préstamos — regla de rol implementada
- Validación de saldo antes de retiro
- `DomainException` personalizada

## ❌ Deficiencias Importantes

- Ausencia de la entidad `User` (acceso al sistema)
- Ausencia de `Transfer` (modelo de transferencias)
- Ausencia de `Bitácora` (auditoría)
- Ausencia de `BankProduct` (catálogo)
- El proyecto está incompleto en alcance pero excelente en profundidad

## 💡 Áreas de Mejora

1. Implementar la entidad `User` con credenciales y `UserStatus`
2. Implementar la entidad `Transfer` con estados y validaciones
3. Implementar `Bitacora` / `OperationLog` con `Map<String, Object>`
4. Agregar catálogo `BankProduct` con `ProductCategory`
5. Agregar enum `Currency` y usarlo en `Money`
6. Completar el README con materia, tecnología e instrucciones de ejecución
