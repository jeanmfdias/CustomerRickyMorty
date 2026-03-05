# Project TODO / Feedback

Items identified during code review. Organized by priority.

---

## Bugs (High Priority)

### 1. Episode parsing — wrong substring indices
**File:** `src/main/java/com/rickymorty/customer/models/RickyMortyEpisode.java`

The API returns episode codes like `"S01E01"`. The current code:
```java
this.season = Integer.valueOf(episode.episode().substring(1, 2));      // "0" — wrong!
this.episodeNumber = Integer.valueOf(episode.episode().substring(4, 5)); // "0" — wrong!
```
Fix:
```java
this.season = Integer.parseInt(episode.episode().substring(1, 3));       // "01" → 1
this.episodeNumber = Integer.parseInt(episode.episode().substring(4, 6)); // "01" → 1
```
Also remove `OptionalInt.of(...)` — it is semantically incorrect here (use `Integer.parseInt` directly).

### 2. HTTP 201 returned even on save failure
**File:** `src/main/java/com/rickymorty/customer/controllers/RickyMortyLocationController.java`

`saveLocationsFromWeb()` always returns HTTP 201 CREATED regardless of success/failure. When `saveWithSuccess` is `false`, it should return HTTP 400 or 500.

---

## Architecture / Design (Medium Priority)

### 3. Services inject concrete classes instead of interfaces
**Files:** `RickyMortyCharacterService.java`, `RickyMortyLocationService.java`

Both services autowire `ConsumerApi` and `TranslateData` directly instead of using the interfaces `IConsumer` and `ITranslate`. The interfaces were defined but are not being used.

```java
// Wrong
@Autowired private ConsumerApi consumerApi;

// Correct
@Autowired private IConsumer consumerApi;
```

### 4. `Main.java` bypasses Spring dependency injection
**File:** `src/main/java/com/rickymorty/customer/view/Main.java`

`Main.java` manually instantiates services with `new ConsumerApi()` and `new TranslateData()`, bypassing the Spring container. It should be a `@Component` with constructor injection.

### 5. Duplicated API URL constant
**Files:** `RickyMortyCharacterService.java`, `RickyMortyLocationService.java`

Both services declare the same constant:
```java
private final String ADDRESS_LOCATION = "https://rickandmortyapi.com/api/location/";
```
Centralize this in `application.properties` or a shared constants class.

### 6. `RickyMortyLocationDTO` uses raw `Map` for residents
**File:** `src/main/java/com/rickymorty/customer/dto/RickyMortyLocationDTO.java`

```java
List<Map<String, Object>> residents  // avoid raw Map
```
Replace with a typed record:
```java
record ResidentSummaryDto(Long id, String name) {}
List<ResidentSummaryDto> residents
```

### 7. Endpoint path mapping may be incorrect
**File:** `src/main/java/com/rickymorty/customer/controllers/RickyMortyCharacterController.java`

`@GetMapping("location-name")` creates the path `/characters/location-name?name=...`. Review if the intended path is `/characters?name=...` (collection + query param) instead.

---

## Spring Best Practices (Low Priority)

### 8. Use constructor injection instead of `@Autowired` on fields
Field injection makes unit testing harder and hides dependencies. Prefer constructor injection in all services and controllers. With Lombok this is trivial with `@RequiredArgsConstructor`.

### 9. Inconsistent JSON annotation usage
**File:** `src/main/java/com/rickymorty/customer/models/RickyMortyEpisodeRecord.java`

Mixes `@JsonAlias` and `@JsonProperty`. Use `@JsonProperty` consistently, especially for snake_case API fields like `air_date`.

### 10. Inconsistent DTO naming
`RickyMortyCharacterDto` uses lowercase `to` while `RickyMortyLocationDTO` uses uppercase. Pick one convention.

### 11. Typo in class/package names
The character's name is **Rick**, not **Ricky**. All classes use `RickyMorty` — consider renaming to `RickMorty` or `RickAndMorty`.

---

## Error Handling (Low Priority)

### 12. Silent exception swallowing
- `ConsumerApi.getData()` — wraps `IOException`/`InterruptedException` in `RuntimeException` with no logging.
- `TranslateData.getData()` — wraps `JsonProcessingException` silently.
- `saveLocationFromWeb()` — catches all `Exception` and returns `false` with no log.

Add logging (use SLF4J `Logger`) before re-throwing, and consider a custom exception hierarchy (`ApiConsumerException`, `DataNotFoundException`).

---

## Security (Low Priority)

### 13. Hardcoded database credentials
**File:** `src/main/resources/application.properties`

`spring.datasource.password=secret` is hardcoded. Use environment variables:
```properties
spring.datasource.password=${DB_PASSWORD:secret}
```

### 14. CORS allows risky HTTP methods
**File:** `src/main/java/com/rickymorty/customer/config/CorsConfiguration.java`

Remove `TRACE` and `CONNECT` from allowed methods — they are not used in REST APIs and can be a security risk.

---

## Missing Features (Low Priority)

### 15. No unit tests
Only a context-load test exists. Add:
- Service tests with Mockito
- Controller tests with `MockMvc`

### 16. No input validation
Endpoints accept any value without validation. Use `@Valid`, `@Positive`, `@NotBlank` with Bean Validation.

### 17. No pagination
`GET /locations` loads all records. Add `Pageable` support via Spring Data.

### 18. No API documentation
Add `springdoc-openapi-starter-webmvc-ui` dependency for Swagger UI at `/swagger-ui.html`.
