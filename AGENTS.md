# AGENTS.md — class-times-record Backend

> Microservice backend for the course/class recording WeChat Mini Program (uni-app).
> Built with Spring Cloud Alibaba (Nacos + Sentinel + Gateway).

## Environment Setup

| Variable | Value | Notes |
|----------|-------|-------|
| `JAVA_HOME` | `D:\JAVA\jdk\jdk21` | JDK 21 (Zulu/Oracle) |
| Maven | 3.9+ | `D:\Application\apache-maven-3.9.12` |
| Nacos Server | `localhost:8848` | Configurable via `${nacos-server-addr}` |
| Sentinel Dashboard | `localhost:8080` | Configurable via `${sentinel-dashboard}` |

```powershell
# Set JAVA_HOME before any Maven command
$env:JAVA_HOME = "D:\JAVA\jdk\jdk21"
```

## Architecture

```
Client → Gateway (:8080) ──lb──→ auth-service (:8081)
                        ──lb──→ business-service (:8082)
                        ↑
                   Nacos (:8848) — service registry
                   Sentinel — flow control / circuit breaking
```

- **gateway** — Spring Cloud Gateway + Nacos Discovery + Sentinel. JWT validation, route dispatch, CORS, rate limiting.
- **auth-service** — Auth, user CRUD, admin, menu/permission (RBAC). Registers with Nacos.
- **business-service** — Institution, student, teacher, class, course, schedule, record. Registers with Nacos.
- **common** — Shared library: entities, DTOs, VOs, converters, utils, filters, interceptors, config.

## Tech Stack

| Layer          | Tech                         | Version       |
|----------------|------------------------------|---------------|
| Framework      | Spring Boot                  | 4.0.4         |
| JDK            | Java                         | 21            |
| Cloud          | Spring Cloud Alibaba         | 2025.1.0.0    |
| Cloud Base     | Spring Cloud                 | 2025.0.1      |
| Registry       | Nacos                        | (Alibaba BOM) |
| Flow Control   | Sentinel                     | (Alibaba BOM) |
| Gateway        | Spring Cloud Gateway         | (Cloud BOM)   |
| ORM            | MyBatis-Plus                 | 3.5.16        |
| Database       | MySQL                        | 8.0+          |
| Auth           | jjwt                         | 0.11.5        |
| Encryption     | BouncyCastle SM2/SM3         | 1.84          |
| Mapping        | MapStruct                    | 1.5.5.Final   |
| JSON           | fastjson2                    | 2.0.60        |
| Docs           | SpringDoc OpenAPI            | 3.0.3         |
| Utils          | Hutool                       | 5.8.40        |
| Build          | Maven                        | 3.9+          |

## Module Map

```
backend/
├── pom.xml                      # Parent POM (multi-module, manages Alibaba + Cloud BOMs)
├── common/                      # Shared library (no main class)
│   ├── annotation/              # @BaseDateTimeToString, @UpdateStudentCount ...
│   ├── aspect/                  # StudentCountAspect
│   ├── common/enums/            # ResultCode
│   ├── config/                  # MyBatisPlusConfig, WebConfig, OpenApiConfig
│   ├── context/                 # UserContext (ThreadLocal)
│   ├── converter/               # MapStruct converters (AdminConverter, UserConverter ...)
│   ├── exception/               # BusinessException, GlobalExceptionHandler
│   ├── filter/                  # RequestCachingFilter, GatewayUserFilter
│   ├── interceptor/             # UserInterceptor, SignInterceptor
│   ├── repository/dto/          # Request DTOs (auth/, course/, student/ ...)
│   ├── repository/entity/       # Entities (User, Student, Class, Record ...)
│   ├── repository/vo/           # Response VOs
│   ├── support/                 # RepeatedlyRequestWrapper
│   └── util/                    # JwtUtils, SM2Util, SM3Util, DateTransformUtils ...
├── gateway/                     # API Gateway (port 8080)
│   ├── filter/JwtAuthFilter     # Reactive JWT validation → X-User-Id header
│   └── resources/application.yml  # Routes via lb://service-name, Nacos, Sentinel
├── auth-service/                # Auth microservice (port 8081, Nacos-registered)
│   └── Controllers: Auth, Menu, PermissionRecord
└── business-service/            # Business microservice (port 8082, Nacos-registered)
    └── Controllers: Institution, Student, Teacher, Class, Course,
                     ClassSchedule, CourseRecord, Record, Test
```

## Request Flow

1. **Gateway** receives the request at `:8080`
2. `JwtAuthFilter` validates the JWT token (skips `/auth/**`, `/swagger/**`, `/test/**`)
3. Gateway looks up service via **Nacos** (`lb://auth-service` or `lb://business-service`)
4. **Sentinel** applies flow control rules if configured
5. Gateway forwards `X-User-Id` and `X-User-Role` headers to downstream service
6. **Service** receives the request — `GatewayUserFilter` reads headers into `UserContext` (ThreadLocal)
7. `SignInterceptor` validates SM3 request signature (skips `/auth/**`, `/test/**`)
8. Controller → Service → Mapper → MySQL

## Coding Conventions

### Package structure
- All code under `com.shiroko` base package
- `com.shiroko.repository.entity` — database entities
- `com.shiroko.repository.dto` — request body DTOs (grouped by domain)
- `com.shiroko.repository.vo` — response VOs
- `com.shiroko.converter` — MapStruct converters (entity ↔ DTO/VO)
- Validation groups in `validategroup/` sub-packages of each DTO package

### Naming
- Controllers: `{Domain}Controller` (e.g., `StudentController`)
- Services: `{Domain}Service` interface + `{Domain}ServiceImpl`
- Mappers: `{Domain}Mapper` (MyBatis-Plus BaseMapper)
- DTOs: `{Action}{Domain}DTO` (e.g., `InsertStudentDTO`, `QueryStudentDTO`)
- VOs: mirror DTO naming with `VO` suffix

### Controllers
- All use `@RestController` + `@RequestMapping`
- All use `@PostMapping` (POST for both reads and writes — non-RESTful convention)
- Request body uses `@RequestBody` + `@Valid`/`@Validated`
- Response wrapped in `ResponseDTO<T>`
- OpenAPI: `@Tag` on class, `@Operation` on method

### Database
- Entity primary keys: `ASSIGN_ID` (snowflake algorithm)
- Logical delete field: `isDeleted` (0 = not deleted, 1 = deleted)
- Underscore-to-camelCase auto mapping enabled
- Insert/update strategy: `NOT_NULL`

### Auth
- Gateway-level JWT validation (public paths configured in gateway)
- SM2 encrypted private key for sensitive data
- SM3 signing for request integrity (`SignInterceptor`)
- `UserContext` (ThreadLocal, servlet services only) for per-request user identity

## Adding a New Feature

1. **Add entity** in `common/src/.../repository/entity/`
2. **Add DTOs/VOs** in `common/src/.../repository/dto/` and `vo/`
3. **Add converter** in `common/src/.../converter/`
4. **Add Mapper interface + XML** in the appropriate service module
5. **Add Service interface + impl** in the service module
6. **Add Controller** in the service module
7. **Add route** in `gateway/src/.../resources/application.yml` if new path prefix

## Building & Running

```powershell
# Prerequisites — set JAVA_HOME
$env:JAVA_HOME = "D:\JAVA\jdk\jdk21"

# Build all modules
mvn clean package -DskipTests

# Run services (start Nacos first!)
# Nacos: startup.cmd -m standalone  (from nacos/bin/)
# Sentinel: java -jar sentinel-dashboard.jar

# Start services in order:
cd gateway && mvn spring-boot:run           # port 8080
cd auth-service && mvn spring-boot:run      # port 8081 (registers with Nacos)
cd business-service && mvn spring-boot:run  # port 8082 (registers with Nacos)
```

## Testing

```powershell
$env:JAVA_HOME = "D:\JAVA\jdk\jdk21"

# Run all tests
mvn test

# Run specific module tests
mvn test -pl auth-service
mvn test -pl business-service
```

- Unit tests use H2 in-memory database (profile: `test`)
- API tests use `@SpringBootTest` + `MockMvc`
- Test config: `src/test/resources/application-test.yml`

## Environment Profiles

| Profile | Config file            | Database host       |
|---------|------------------------|---------------------|
| dev     | application-dev.yml    | 121.196.229.10      |
| prod    | application-prod.yml   | localhost           |
| test    | application-test.yml   | H2 in-memory        |

## Spring Cloud Alibaba Components

### Nacos (Service Discovery)
- Services register themselves at startup via `spring.cloud.nacos.discovery`
- Gateway resolves `lb://service-name` URIs via Nacos
- Server address: `${nacos-server-addr:localhost:8848}`

### Sentinel (Flow Control)
- Gateway-level rate limiting via `spring.cloud.alibaba.sentinel.gateway`
- Dashboard: `${sentinel-dashboard:localhost:8080}`
- Rules can be configured via Sentinel Dashboard UI

### Common Module Note
- `common` includes `spring-boot-starter-web` (servlet) for auth/business services
- Gateway excludes it (`spring-boot-starter-web`, `springdoc-openapi-starter-webmvc-ui`)
  to avoid conflict with reactive WebFlux

## Git Conventions

- Commit messages in **Chinese**
- Prefix with verb: `fix`, `feat`, `refactor`, `docs`, `test`, `chore`
- Format: `type(scope): short description`, then blank line, then bullet details

## Key Files

| File | Purpose |
|------|---------|
| `pom.xml` | Parent POM — manages Alibaba + Cloud BOMs |
| `gateway/.../JwtAuthFilter.java` | Reactive Gateway JWT validation filter |
| `common/.../filter/GatewayUserFilter.java` | Reads X-User-Id into UserContext (servlet) |
| `common/.../config/WebConfig.java` | MVC config (filters, interceptors) |
| `common/.../util/JwtUtils.java` | JWT token generation & validation |
| `common/.../util/SM2Util.java` | SM2 encryption/decryption |
| `common/.../util/SM3Util.java` | SM3 hashing & signing |
| `common/.../exception/handler/GlobalExceptionHandler.java` | Unified exception handling |
| `docs/class_times_record.sql` | Full database schema |
| `pipeline/Jenkinsfile` | CI/CD pipeline |
