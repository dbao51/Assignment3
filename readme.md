# AWE Electronics Store  
SWE30003 – Assignment 3 Web Implementation

## 1. Project Overview

AWE Electronics Store is a small web-based online shop used for Assignment 3.  
The implementation focuses on four core business areas:

- **Catalogue** – browse, search, filter and sort products.
- **Cart** – add/remove items and update quantities.
- **Checkout** – validate customer and address details and create orders.
- **Payment Simulation** – simulate payment processing and show an order confirmation message (no real payment is performed).

The application is implemented in **Java** using **Spring Boot** and runs as a simple web app on `http://localhost:8080`.

---

## 2. Technology Stack & Dependencies

**Language & Frameworks**

- Java 17 (or compatible LTS version)
- Spring Boot (Web MVC)
- Thymeleaf (server-side HTML rendering)
- JUnit 5 / Spring Boot Test (unit testing)

**Key Maven dependencies** (see `pom.xml` for exact versions):

- `spring-boot-starter-web` – REST controllers, servlet stack, embedded Tomcat.
- `spring-boot-starter-thymeleaf` – template engine for HTML pages.
- `spring-boot-starter-test` – testing support (JUnit, AssertJ, etc.).

All other dependencies are transitive from these starters.

---

## 3. How to Build and Run

### 3.1 Requirements

- **OS:** macOS (tested on macOS with zsh).
- **IDE:** Visual Studio Code (with Java extensions) – used for development.
- **Java:** JDK 17 or higher on the `PATH` (`java -version`).
- **Build Tool:** Maven (`mvn -version`).

### 3.2 Run from Command Line (recommended for marking)

From the project root (where `pom.xml` is located):

```bash
# 1. Run the tests
mvn test

# 2. Build the executable jar
mvn clean package

# 3. Run the application
# Replace the jar name with the one generated in the target folder if different
java -jar target/*.jar
