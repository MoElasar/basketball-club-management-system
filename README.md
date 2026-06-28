# Basketball Club Management System

A full-stack web application for running a basketball club end to end — players, teams, games, staff, contracts, merchandise, ticketing, and sponsorships — with separate admin and customer-facing experiences. Built with Spring Boot and secured with JWT-based authentication.

> University project — Management Information Systems, Işık University.

## Features

- **Player management** — player profiles, statistics, contracts, and awards.
- **Team & staff management** — teams, rosters, and staff records.
- **Games & fixtures** — scheduling and tracking matches.
- **Merchandise store** — product catalogue and customer order management.
- **Ticketing** — match ticket handling.
- **Sponsorships** — sponsor records and sponsorship deals.
- **News** — club news posts.
- **Facilities** — facility management.
- **Authentication** — JWT-based auth with admin and customer roles (Spring Security).
- **File uploads** — player profile images served through a dedicated file endpoint.

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Security, JWT (jjwt), Spring Data JPA (Hibernate)
- **Frontend:** Thymeleaf, HTML, CSS
- **Database:** MySQL
- **Build:** Maven

## Getting Started

### Prerequisites
- Java 17+
- MySQL running locally
- Maven (or use the included `mvnw` wrapper)

### Setup

1. Create a MySQL database:
   ```sql
   CREATE DATABASE bms_db;
   ```
2. Copy the example properties and fill in your values:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
   Then set your MySQL username/password and a JWT secret.
3. Set the JWT secret as an environment variable (recommended) instead of hardcoding it:
   ```bash
   export JWT_SECRET="a-long-random-string-at-least-64-characters"
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
5. Open `http://localhost:8080` in your browser.

The schema is created automatically on first run, and a default data initializer seeds initial records.

## Project Structure

```
src/main/java/com/learning/main/
├── config/        # Security, web, and data-initializer configuration
├── controllers/   # Request handlers (players, games, merchandise, etc.)
├── model/         # JPA entities
├── repository/    # Spring Data repositories
├── service/       # Business logic
├── security/      # JWT and authentication
└── enm/           # Enums
```

## Security Note

The JWT secret is read from the `JWT_SECRET` environment variable (with a local-only fallback). Never commit a real production secret to source control.
