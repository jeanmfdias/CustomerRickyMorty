# Rick and Morty API — Spring Boot Study Project

A Spring Boot REST API that integrates with the [Rick and Morty public API](https://rickandmortyapi.com/) to fetch, persist, and query characters, locations, and episodes.

---

## Tech Stack

- **Java 21** + **Spring Boot 3.4.1**
- **Spring Data JPA** + **PostgreSQL**
- **Jackson** for JSON deserialization
- **JUnit 5** + **Mockito** + **MockMvc** for testing
- **Docker** + **Docker Compose**

---

## Project Structure

```
src/main/java/com/rickymorty/customer/
├── config/         # CORS configuration
├── contracts/      # IConsumer and ITranslate interfaces
├── controllers/    # REST endpoints
├── dto/            # Response DTOs
├── models/         # JPA entities and API record types
├── repositories/   # Spring Data JPA repositories
├── services/       # Business logic
└── view/           # Legacy CLI interface (Main.java)
```

---

## REST Endpoints

### Characters
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/characters/save-by-location/{locationId}` | Fetch characters from the API for a saved location and persist them |
| `GET`  | `/characters/location-name?name=` | List characters by location name |
| `GET`  | `/characters/location/{locationId}?name=&page=0&size=10` | List characters by location ID (paginated, optional name filter) |
| `GET`  | `/characters/name?name=` | Find a single character by name |

### Locations
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/locations` | List all saved locations with their residents |
| `POST` | `/locations/save-from-web/{locationId}` | Fetch a location from the API by its ID and persist it |

### Episodes
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/episodes?name=&page=0&size=10` | List episodes sorted by air date (paginated, optional name filter). Returns `404` if none found. |

---

## Running the Project

### Prerequisites
- [Docker](https://www.docker.com/) and Docker Compose installed

### 1. Clone and configure

```bash
git clone <repo-url>
cd CustomerRickyMorty
cp .env.example .env   # adjust credentials if needed
```

### 2. Start the application

```bash
docker-compose up --build
```

This starts two containers:
- **postgres** — PostgreSQL 16 database on port `5432`
- **app** — Spring Boot application on port `8081`

The app is available at `http://localhost:8081`.

### 3. Populate data (quick start)

```bash
# Save a location from the Rick and Morty API (e.g., location ID 1 = Earth C-137)
curl -X POST http://localhost:8081/locations/save-from-web/1

# Save the characters that live in that location
curl -X POST http://localhost:8081/characters/save-by-location/1

# List all locations
curl http://localhost:8081/locations

# List characters in location 1
curl http://localhost:8081/characters/location/1

# Search episodes
curl http://localhost:8081/episodes
```

### 4. Stop the application

```bash
docker-compose down
```

To also remove the database volume:
```bash
docker-compose down -v
```

---

## Running the Tests

Tests are split into:
- **Unit tests** — service layer with Mockito (no database required)
- **Controller tests** — web layer with `@WebMvcTest` + `MockMvc` (no database required)

### Option 1: Run with Docker (recommended)

```bash
docker-compose --profile test run --rm test
```

This starts a PostgreSQL container and runs `mvn test` inside a Maven container. Maven dependencies are cached in a named volume for faster subsequent runs.

### Option 2: Run locally with Maven

Requires a running PostgreSQL instance matching the settings in `application.properties` (or set environment variables).

```bash
./mvnw test
```

### Test structure

```
src/test/java/com/rickymorty/customer/
├── CustomerApplicationTests.java              # Context load test
├── services/
│   ├── RickAndMortyCharacterServiceTest.java  # 9 unit tests
│   ├── RickAndMortyLocationServiceTest.java   # 4 unit tests
│   └── RickAndMortyEpisodeServiceTest.java    # 4 unit tests
└── controllers/
    ├── RickAndMortyCharacterControllerTest.java # 7 tests
    ├── RickAndMortyLocationControllerTest.java  # 4 tests
    └── RickAndMortyEpisodeControllerTest.java   # 5 tests
```

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:postgresql://localhost/customer_ricky_morty` | JDBC connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `secret` | Database password |
