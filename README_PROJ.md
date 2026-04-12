# Commerce API - Project Deep-Dive

This document explains what this project is, how it is designed, how control flows through it, and how to run, stop, and test it.

## 1) What This Project Is

`commerce-api` is a small Spring Boot REST backend prototype for a commerce domain.

Current scope:
- Welcome endpoint (`/`, `/home`)
- Product listing endpoint (`/api/products`)
- In-memory cart endpoints (`/api/cart/add`, `/api/cart/view`, `/api/cart/checkout`)
- Health/mappings observability via Spring Actuator (`/actuator/health`, `/actuator/mappings`)

It is intentionally simple and currently has:
- No database (all cart data is in memory)
- No authentication/authorization
- No service/repository layers yet (controllers hold logic)

## 2) Tech Stack and Build

From `pom.xml`:
- Java: `1.8`
- Spring Boot: `2.7.18`
- Dependencies:
  - `spring-boot-starter-web`
  - `spring-boot-starter-actuator`
  - `spring-boot-starter-test`
- Build tool: Maven Wrapper (`mvnw`, `mvnw.cmd`)

## 3) Project Structure and Component Responsibilities

Top-level important files:
- `src/main/java/com/example/commerceapi/CommerceApiApplication.java`
- `src/main/java/com/example/commerceapi/RootController.java`
- `src/main/java/com/example/commerceapi/product/Product.java`
- `src/main/java/com/example/commerceapi/product/ProductController.java`
- `src/main/java/com/example/commerceapi/cart/CartController.java`
- `src/main/resources/application.properties`
- `src/test/java/com/example/commerceapi/*.java`

Component-level responsibilities:

1. `CommerceApiApplication`
- Entry point.
- `main()` calls `SpringApplication.run(...)`.
- `@SpringBootApplication` enables component scan and auto-configuration.

2. `RootController`
- REST controller for root/home paths.
- Endpoints:
  - `GET /`
  - `GET /home`
  - `GET /home/`
- Returns plain text: `"Welcome to the Commerce API"`.

3. `Product` (POJO model)
- Simple data model with fields:
  - `id` (int)
  - `name` (String)
  - `price` (double)
- Used as JSON response payload object.

4. `ProductController`
- Base mapping: `/api`
- Endpoint:
  - `GET /api/products`
- Returns hardcoded product list (3 items).

5. `CartController`
- Base mapping: `/api/cart`
- In-memory store:
  - `Map<String, List<Integer>> carts = new ConcurrentHashMap<>()`
- Endpoints:
  - `POST /api/cart/add?session={session}&id={productId}`
    - Adds product id into session cart.
    - Returns JSON with `ok`, `count`, `session`.
  - `GET /api/cart/view?session={session}`
    - Returns JSON with `items`, `session`, `size`.
  - `POST /api/cart/checkout?session={session}`
    - Removes cart.
    - If cart empty/not found -> `400` with error JSON.
    - Else -> returns `status=OK`, random `orderId`, and `items`.
- Utility method `map(Object... kv)` builds ordered response maps.

6. `application.properties`
- `spring.application.name=commerce-api`
- `server.port=8080`
- TRACE logging for mapping registration:
  - `logging.level.org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping=TRACE`
- Exposes actuator endpoints:
  - `management.endpoints.web.exposure.include=mappings,health`

## 4) Where Control Starts and Request Flow

### 4.1 Startup control flow
1. JVM starts `CommerceApiApplication.main()`.
2. Spring Boot bootstraps application context.
3. Component scan finds `@RestController` classes.
4. DispatcherServlet is configured.
5. Request mappings are registered:
- `/`, `/home`, `/home/`
- `/api/products`
- `/api/cart/add`, `/api/cart/view`, `/api/cart/checkout`
6. Embedded Tomcat starts (default port `8080` unless overridden).

### 4.2 Runtime HTTP flow (generic)
1. Client calls endpoint.
2. Tomcat receives request.
3. Spring `DispatcherServlet` routes request by mapping.
4. Target controller method executes.
5. Return value is serialized to HTTP response (JSON/string).
6. Response sent back to client.

### 4.3 Cart flow example
1. `POST /api/cart/add?session=demo&id=1`
- If `demo` does not exist, it is created.
- `1` is appended to list.
2. `GET /api/cart/view?session=demo`
- Returns items currently in memory for `demo`.
3. `POST /api/cart/checkout?session=demo`
- Session cart is removed from map.
- Returns generated order ID.

## 5) API Endpoints (Current)

Application endpoints:
- `GET /`
- `GET /home`
- `GET /home/`
- `GET /api/products`
- `POST /api/cart/add?session=<name>&id=<productId>`
- `GET /api/cart/view?session=<name>`
- `POST /api/cart/checkout?session=<name>`

Actuator endpoints (enabled):
- `GET /actuator/health`
- `GET /actuator/mappings`

## 6) How to Start

From project root (`commerce-api`):

Using Maven Wrapper (recommended):
```powershell
./mvnw spring-boot:run
```

Or run packaged jar:
```powershell
./mvnw clean package
java -jar target/commerce-api-0.0.1-SNAPSHOT.jar
```

Expected startup:
- App listens on `http://localhost:8080`

Quick verification:
```powershell
curl http://localhost:8080/
curl http://localhost:8080/api/products
curl http://localhost:8080/actuator/health
```

## 7) How to Stop

If running in foreground terminal:
- Press `Ctrl + C`

If running from IDE:
- Stop/Terminate the running Spring Boot process.

## 8) How to Test Everything

Run all tests:
```powershell
./mvnw test
```

Run one test class:
```powershell
./mvnw -Dtest=SmokeTest test
./mvnw -Dtest=CartFlowTest test
```

## 9) Current Test Status

All 3 tests pass:

1. `CommerceApiApplicationTests` ✅
- Context loads successfully.

2. `SmokeTest` ✅
- Tests: `GET /actuator/health`
- Expected: `200 OK`

3. `CartFlowTest` ✅
- Tests: `POST /api/cart/add`, `GET /api/cart/view`, `POST /api/cart/checkout`
- Expected: `200 OK` with correct response payloads

## 10) Design Notes and Limitations

Current design characteristics:
- Stateless app server process except in-memory cart map.
- Cart data is process-local and volatile.
- On restart, all cart sessions are lost.
- No product persistence; product catalog is hardcoded.
- No validation for product existence when adding to cart.

## 11) Suggested Next Refactor Direction

1. Introduce service layer
- `ProductService`, `CartService` to move business logic out of controllers.

2. Introduce repository/persistence
- Start with JPA + H2/PostgreSQL for products/carts/orders.

3. Add request/response DTOs and validation
- Use `@Valid` and bean validation annotations.

4. Improve test suite
- Fix endpoint paths.
- Add negative tests for checkout and invalid inputs.
- Add controller tests (`@WebMvcTest`) and integration tests.

5. Add API documentation
- OpenAPI/Swagger for discoverable contracts.

---

If you want, next I can also generate a sequence diagram style section (text + Mermaid) and add it into this file for visual flow clarity.
