# E-Store

Spring Boot REST API za onlajn prodavnicu (e-commerce sistem) sa upravljanjem korisnicima, proizvodima, prodavnicama i porudžbinama.

## Tehnologije

- **Java 26**, **Spring Boot 4.1.0**
- **Spring Data JPA** / Hibernate
- **MySQL 8** — baza podataka
- **Flyway** — verzionisanje šeme baze
- **Spring Security** + **JWT** — autentikacija i autorizacija
- **Spring AMQP** / **RabbitMQ** — asinhrona obrada (notifikacije o porudžbinama)
- **Spring Boot Actuator** + **Micrometer** — health check i metrike
- **springdoc-openapi** — Swagger/OpenAPI dokumentacija
- **Docker** / **Docker Compose** — kontejnerizacija
- **JUnit 5**, **Mockito**, **Testcontainers** — testiranje

## Struktura projekta

```
src/main/java/rs/ac/ni/pmf/rwa/estore/
├── controller/     REST kontroleri
├── service/        Poslovna logika
├── repository/     Spring Data JPA repozitorijumi
├── model/
│   ├── entity/      JPA entiteti
│   └── dto/         Request/Response DTO klase
├── security/       JWT, UserDetails, SecurityConfig
├── exception/      Globalni exception handler i custom izuzeci
├── config/         Konfiguracione klase (RabbitMQ, Async, itd.)
└── health/         Custom Actuator health indikatori

src/main/resources/
├── db/migration/    Flyway migracije
└── application.properties
```

## Pokretanje

### Preduslovi

- Java 26
- Maven
- MySQL 8 (lokalno) ili Docker

### Lokalno (bez Docker-a)

1. Kreiraj MySQL bazu `e_store`.
2. Podesi konekciju u `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/e_store?useSSL=false&serverTimezone=Europe/Belgrade&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=root
   ```
3. Pokreni aplikaciju:
   ```bash
   mvn spring-boot:run
   ```
   Flyway će automatski kreirati šemu baze pri prvom pokretanju.

### Docker (aplikacija + MySQL + RabbitMQ)

```bash
docker-compose up --build
```

Ovo podiže tri kontejnera:
- `e_store-mysql` — MySQL baza (dostupna spolja na portu `3307`)
- `e_store-rabbitmq` — RabbitMQ (management UI na `http://localhost:15672`, login `guest`/`guest`)
- `e_store-app` — sama aplikacija (port `8080`)

Zaustavljanje:
```bash
docker-compose down
```

Potpuni reset (uključujući podatke u bazi):
```bash
docker-compose down -v
```

## API dokumentacija

Nakon pokretanja aplikacije, Swagger UI je dostupan na:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI specifikacija (JSON):

```
http://localhost:8080/v3/api-docs
```

## Autentikacija

API koristi JWT tokene. Tok rada:

1. `POST /api/v1/auth/login` sa `username`/`password` → vraća `accessToken` i `refreshToken`.
2. Za sve zaštićene rute, dodaj header:
   ```
   Authorization: Bearer <accessToken>
   ```

### Uloge korisnika

| Uloga     | Opis                                                          |
|-----------|----------------------------------------------------------------|
| `User`    | Kupovina, porudžbine                                          |
| `Manager` | Upravljanje sopstvenom prodavnicom (cene, zalihe)              |
| `Admin`   | Puna administracija (korisnici, prodavnice, proizvodi, kategorije) |

## Monitoring

- Health check: `GET /actuator/health`
- Metrike: `GET /actuator/metrics`

## Testiranje

```bash
mvn test
```

Projekat sadrži unit testove (JUnit 5 + Mockito) za servisni sloj i integracione testove (Testcontainers + MockMvc) za REST endpoint-e.

## Glavne funkcionalnosti

- CRUD operacije za korisnike, proizvode, prodavnice i kategorije
- Upravljanje zalihama proizvoda po prodavnicama (`store_products`)
- Kreiranje porudžbina i checkout sa proverom/umanjenjem zaliha
- Paginacija, sortiranje i napredno filtriranje (JPA Specifications) na listing endpoint-ima
- Asinhrono slanje notifikacije o završenoj porudžbini preko RabbitMQ
- Verzionisanje šeme baze preko Flyway migracija
- JWT autentikacija i autorizacija zasnovana na ulogama
