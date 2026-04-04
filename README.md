# Sistema de Gestión Bancaria - API Backend

Este repositorio contiene el código fuente para el sistema backend de gestión bancaria, desarrollado aplicando principios de **Diseño Dirigido por el Dominio (DDD)** y **Arquitectura Hexagonal**.

## Información Académica
* **Materia:** Construcción de Software II
* **Fase del Proyecto:** Entrega 2 (Capa de Dominio, Servicios y Modelado)

### 👥 Integrantes del Equipo
* **Karen Ospina**
* **Jorge Andrés Gómez**

---

## Stack Tecnológico
El proyecto está construido utilizando las siguientes tecnologías y herramientas:
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.x
* **Gestor de Dependencias:** Maven
* **Bases de Datos (Híbrida):**
  * **MySQL:** Persistencia transaccional y oficial (Entidades financieras).
  * **MongoDB:** Persistencia NoSQL para la bitácora de auditoría (flexibilidad de logs).

---

## Arquitectura y Patrones de Diseño
El sistema se diseñó estrictamente bajo los lineamientos de la **Arquitectura Hexagonal (Ports and Adapters)**:
1. **Dominio Aislado:** La carpeta `app.domain` no tiene dependencias de frameworks externos (excepto anotaciones básicas de Spring para los servicios).
2. **DDD (Domain-Driven Design):** * Uso de **Entidades** para cuentas y préstamos.
   * Uso de **Value Objects (Objetos de Valor)** como `Money` para encapsular la lógica de las monedas (COP, USD) y montos, garantizando inmutabilidad y operaciones matemáticas seguras.
3. **Puertos (Ports):** Interfaces que definen los contratos que la infraestructura debe cumplir.

---

## Instrucciones de Ejecución

### Prerrequisitos
* Java Development Kit (JDK) 21 instalado.
* Apache Maven instalado.
* (Opcional para esta entrega) Instancias de MySQL y MongoDB corriendo localmente.

### Pasos para ejecutar localmente
1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/Andresgomez20/ConstruccionDeSoftware2JorgeGomez.git
   cd ConstruccionDeSoftware2-LM8