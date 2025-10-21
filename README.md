# Restaurant Management System - Backend

## RESTful API für ein Restaurant-Management-System. Verwaltet Reservierungen, Tische und Kunden.

## Technologien

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Maven

## Voraussetzungen

- JDK 17 oder höher
- PostgreSQL 12+
- Maven 3.6+

## Installation

```bash
git clone [repository-url]
cd restaurant-backend
mvn clean install
```

## Konfiguration

Erstelle eine application.properties in src/main/resources:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/restaurant_db
spring.datasource.username=dein_username
spring.datasource.password=dein_passwort
spring.jpa.hibernate.ddl-auto=update
Starten
bashmvn spring-boot:run
```

Die API läuft dann auf `http://localhost:8080`

## API Endpoints

### Reservierungen
- `GET /api/reservations` - Alle Reservierungen
- `GET /api/reservations/{id}` - Einzelne Reservierung
- `POST /api/reservations` - Neue Reservierung erstellen
- `PUT /api/reservations/{id}` - Reservierung aktualisieren
- `DELETE /api/reservations/{id}` - Reservierung löschen

### Tische
- `GET /api/tables` - Alle Tische
- `GET /api/tables/{id}` - Einzelner Tisch
- `POST /api/tables` - Neuen Tisch erstellen

### Kunden
- `GET /api/customers` - Alle Kunden
- `POST /api/customers` - Neuen Kunden erstellen

## Projekt-Struktur

```
src/main/java/
├── controller/     # REST Endpoints
├── service/        # Business Logic
├── repository/     # Datenbankzugriff
├── model/          # Entities
└── dto/            # Data Transfer Objects
```

## Deployment
Das Projekt ist bereit für Deployment auf Railway, Render oder ähnlichen Plattformen.
## Entwicklung

```bash
# Tests ausführen
mvn test
```

## Build erstellen
mvn clean package
