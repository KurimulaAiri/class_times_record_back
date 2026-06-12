# AGENTS.md — 教学录播课表记录后端

基于 Spring Cloud Alibaba 微服务架构的教学管理系统后端。

## 项目结构

```
backend/
├── common/                # 共享代码库：Entity、DTO、VO、Converter、Service 接口、工具类
│   └── src/main/java/com/shiroko/
│       ├── annotation/    # 自定义注解（BaseDateTimeToString、UpdateStudentCount 等）
│       ├── common/enums/  # 通用枚举（ResultCode）
│       ├── config/        # 通用配置（MyBatisPlusConfig、OpenApiConfig、WebConfig）
│       ├── context/       # 线程上下文（UserContext — ThreadLocal 用户信息）
│       ├── converter/     # MapStruct 转换器接口（@Mapper(componentModel="spring")）
│       ├── exception/     # 异常定义 + 全局异常处理器（GlobalExceptionHandler）
│       ├── filter/        # Servlet 过滤器（GatewayUserFilter、RequestCachingFilter）
│       ├── interceptor/   # 通用拦截器（SignInterceptor 签名校验、UserInterceptor 上下文清理）
│       ├── repository/
│       │   ├── dto/       # 数据传输对象（按业务分子目录：admin/、auth/、student/ 等）
│       │   ├── entity/    # MyBatis-Plus 实体类（继承 BaseEntity，@TableName 指定表名）
│       │   │   └── common/  # 基础实体（BaseEntity、RoleBaseEntity）
│       │   └── vo/        # 视图对象（按业务分子目录，返回给前端）
│       ├── service/       # 业务 Service 接口（跨模块共享）
│       ├── support/       # 辅助类（RepeatedlyRequestWrapper）
│       └── util/          # 工具类
│           ├── JwtUtils.java           # JWT 令牌工具（AccessToken + RefreshToken）
│           ├── SM2Util.java            # 国密 SM2 非对称加解密
│           ├── SM3Util.java            # 国密 SM3 哈希摘要（带盐值）
│           ├── SM2KeyGenerator.java    # SM2 密钥对生成器
│           ├── DateTransformUtils.java # 日期格式转换
│           └── InstitutionCodeUtil.java# 机构编码生成
├── gateway/               # Spring Cloud Gateway（端口 9999，服务名 cr-gateway）
│   └── src/main/java/com/shiroko/gateway/
│       ├── config/        # 路由配置（RouteConfig — 编程式路由）、CORS
│       └── filter/        # JwtAuthFilter（JWT 校验 + 公开路径放行）
├── auth-service/          # 认证授权微服务（端口 10002，服务名 cr-auth-service）
│   └── src/main/java/com/shiroko/
│       ├── config/        # AuthWebConfig（注册 JwtInterceptor + SignInterceptor + UserInterceptor）
│       ├── controller/    # AuthController、MenuController、PermissionRecordController
│       ├── interceptor/   # JwtInterceptor（小程序 JWT 鉴权，依赖 UserService + UserConverter）
│       ├── mapper/        # UserMapper、UserAuthMapper、TeacherMapper、ParentMapper 等
│       └── service/       # AuthService、UserService、IdentityService 等 + impl
├── business-service/      # 核心业务微服务（端口 10001，服务名 cr-business-service）
│   └── src/main/java/com/shiroko/
│       ├── aspect/        # AOP 切面（StudentCountAspect）
│       ├── controller/    # Institution、Student、Teacher、Course、Class 等业务 Controller
│       ├── mapper/        # 各业务表 Mapper 接口 + XML
│       └── service/       # 各业务 Service 接口 + impl
├── admin-service/         # 管理后台微服务（端口 10003，服务名 cr-admin-service）
│   └── src/main/java/com/shiroko/
│       ├── config/        # AdminWebConfig（注册 AdminJwtInterceptor + UserInterceptor）
│       ├── controller/    # SysUserController、SysRoleController、SysMenuController、SysDashboardController
│       ├── interceptor/   # AdminJwtInterceptor（管理后台 JWT 鉴权，依赖 SysUserService）
│       ├── mapper/        # SysUserMapper、SysRoleMapper、SysMenuMapper 等
│       └── service/       # SysUserService、SysRoleService、SysMenuService、SysDashboardService + impl
├── nacos-config/          # Nacos 配置中心文件（需上传至 Nacos 命名空间 course-record）
│   ├── common-db.yaml         # 共享数据库 + MyBatis-Plus 配置
│   ├── common-sentinel.yaml   # 共享 Sentinel 流控配置
│   ├── cr-gateway.yaml        # Gateway 专属配置（路由、JWT、CORS）
│   ├── cr-auth-service.yaml   # Auth 专属配置（端口、JWT、微信、SM2）
│   ├── cr-business-service.yaml # Business 专属配置（端口、JWT、SM2）
│   └── cr-admin-service.yaml  # Admin 专属配置（端口、JWT）
├── docker-compose.yml     # 生产环境容器编排（network_mode: host）
├── docker-compose.local.yml # 本地开发容器编排（bridge 网络 + Nginx）
├── pipeline/Jenkinsfile   # Jenkins CI/CD 流水线
├── docs/                  # 文档和初始化 SQL
└── AGENTS.md              # 本文件
```

## 技术栈

| 组件 | 技术 | 版本 |
|------|------|------|
| 语言 | Java | 21 |
| 框架 | Spring Boot | 4.0.4 |
| 微服务 | Spring Cloud | 2025.1.1 |
| 微服务治理 | Spring Cloud Alibaba | 2025.1.0.0 |
| 网关 | Spring Cloud Gateway (WebFlux) | 5.0.1 |
| 服务注册/配置 | Nacos | 2.x |
| 流量控制 | Sentinel | 1.8.x |
| 数据访问 | MyBatis-Plus | 3.5.16 |
| 数据库 | MySQL | 8.x |
| 对象映射 | MapStruct | 1.5.5.Final |
| 密码编码 | Spring Security Crypto (BCrypt) | 7.0.4 |
| 国密算法 | Bouncy Castle (SM2/SM3) | — |
| 构建工具 | Maven 多模块 | — |
| 容器化 | Docker + Docker Compose | — |
| CI/CD | Jenkins (Docker) | — |

## Nacos 配置中心

### 命名空间

- 命名空间 ID：`course-record`
- Group：`DEFAULT_GROUP`

### 配置文件上传

所有 Nacos 配置文件存放在 `nacos-config/` 目录，文件头部注释包含上传说明：

| Data ID | 文件 | 说明 |
|---------|------|------|
| `common-db.yaml` | 数据库 + MyBatis-Plus | auth/business/admin 共用 |
| `common-sentinel.yaml` | Sentinel 流控 | 所有服务共用 |
| `cr-gateway.yaml` | Gateway 路由 + JWT + CORS | Gateway 专属 |
| `cr-auth-service.yaml` | 端口 + JWT + 微信 + SM2 | Auth 专属 |
| `cr-business-service.yaml` | 端口 + JWT + SM2 | Business 专属 |
| `cr-admin-service.yaml` | 端口 + JWT | Admin 专属 |

### 本地 application.yml

各服务的 `application.yml` 仅保留 Nacos 连接信息 + `spring.config.import` 引用，业务配置全部由 Nacos 下发：

```yaml
spring:
  application:
    name: cr-xxx
  config:
    import:
      - optional:nacos:cr-xxx.yaml?group=DEFAULT_GROUP&refresh=true
      - optional:nacos:common-db.yaml?group=DEFAULT_GROUP&refresh=true
      - optional:nacos:common-sentinel.yaml?group=DEFAULT_GROUP&refresh=true
  cloud:
    nacos:
      server-addr: ${NACOS_SERVER_ADDR:nacos.kurimula-airi.top}
      discovery:
        namespace: ${NACOS_NAMESPACE:course-record}
      config:
        namespace: ${NACOS_NAMESPACE:course-record}
        file-extension: yaml
```

## common 包工具类

### 加密工具

| 工具类 | 算法 | 用途 | 使用场景 |
|--------|------|------|----------|
| `SM2Util` | 国密 SM2 非对称加密 | 前端密码加密传输 → 后端解密 | 登录/注册时密码解密 |
| `SM3Util` | 国密 SM3 哈希摘要 | 密码存储（带盐值） | `SM3Util.digestWithSalt(password, salt)` |
| `SM2KeyGenerator` | SM2 密钥对生成 | 生成 SM2 公私钥对 | 密钥初始化 |
| `JwtUtils` | HMAC-SHA256 | Access Token / Refresh Token 生成与校验 | 全链路鉴权 |
| BCrypt (Spring Security Crypto) | BCrypt | 管理后台密码存储 | admin-service SysUser 登录 |

**密码流程**：
- 小程序端：前端 SM2 公钥加密 → 后端 `SM2Util.decrypt()` 解密 → `SM3Util.digestWithSalt()` 哈希存储
- 管理后台端：前端明文传输（HTTPS）→ 后端 `BCryptPasswordEncoder.encode()` 哈希存储

### 其他工具

| 工具类 | 用途 |
|--------|------|
| `DateTransformUtils` | 日期格式转换 |
| `InstitutionCodeUtil` | 机构邀请码生成 |
| `UserContext` | ThreadLocal 用户上下文（请求级） |

## 跨模块约定

### DTO / VO / Entity / Converter

- `repository/dto/` 下的 DTO 类使用 `@Data` 注解，按业务分子目录
- `repository/vo/` 下的 VO 类使用 `@Data` 注解，返回前端
- `repository/entity/` 下的实体类继承 `BaseEntity`（含 id、创建/更新时间、逻辑删除），使用 `@TableName` 指定表名
- `converter/` 下的 Converter 接口使用 `@Mapper(componentModel = "spring")`

### Service

- Service 接口定义在 `common/src/.../service/` 中
- ServiceImpl 实现在各微服务模块中
- 使用 `@Service` 注解标记实现类

### Controller

- 使用 `@RestController` + `@RequestMapping`
- 统一返回 `ResponseDTO<T>` 封装
- Auth Service 路径前缀 `/auth/auth/`，Business Service 路径前缀 `/biz/`，Admin Service 路径前缀 `/admin/`

### Gateway 路由

路由通过编程式 Java 配置定义（`gateway/src/.../config/RouteConfig.java`）：

```
/auth/**  → StripPrefix=1 → lb://cr-auth-service
/biz/**   → StripPrefix=1 → lb://cr-business-service
/admin/** → StripPrefix=1 → lb://cr-admin-service
```

Gateway 依赖 `spring-cloud-starter-loadbalancer` 实现 `lb://` 服务发现。

### 安全

- `JwtAuthFilter` (Gateway) — 统一 JWT 校验，公开路径放行
- `JwtInterceptor` (auth-service) — 小程序鉴权，依赖 UserService + UserConverter
- `AdminJwtInterceptor` (admin-service) — 管理后台鉴权，依赖 SysUserService
- `SignInterceptor` (common) — API 签名校验（auth-service、business-service 使用）
- `UserInterceptor` (common) — 请求结束清理 UserContext（所有服务使用）
- `GatewayUserFilter` (common) — 读取网关转发的 X-User-Id/X-User-Role 头设置 UserContext

### 各服务 WebConfig

- auth-service: `AuthWebConfig` — 注册 JwtInterceptor + SignInterceptor + UserInterceptor（排除 common 的 WebConfig）
- admin-service: `AdminWebConfig` — 注册 AdminJwtInterceptor + UserInterceptor（排除 SignInterceptor + WebConfig）
- business-service: 使用 common 的 `WebConfig`（注册 SignInterceptor + UserInterceptor）

### IdentityService（auth-service 内部）

auth-service 直接操作 Teacher/Parent 表查询身份信息，不再通过 Feign 调用 business-service：
- `IdentityService.getByUserId(roleName, userId)` — 查询角色记录
- `IdentityService.checkAvailable(roleName, userId)` — 检查身份是否可用
- `IdentityService.createIdentity(roleName, userId)` — 创建身份记录

## Adding a New Feature

1. **Determine domain**: auth（认证授权）、business（核心业务）、admin（管理后台）
2. **Add entity** in `common/src/.../repository/entity/`
3. **Add DTOs/VOs** in `common/src/.../repository/dto/` 和 `vo/`（按业务分子目录）
4. **Add converter** in `common/src/.../converter/`
5. **Add Service interface** in `common/src/.../service/`
6. **Add Mapper interface + XML** in 对应微服务模块
7. **Add ServiceImpl** in 对应微服务模块
8. **Add Controller** in 对应微服务模块
9. **Add route** in `gateway/src/.../config/RouteConfig.java`（如需新路径前缀）
10. **Add Nacos config** in `nacos-config/`（如需新配置项，上传至 Nacos 命名空间 course-record）

## Building & Running

### 部署架构

服务器上已有以下基础设施（非 Docker 容器）：

| 基础设施 | 地址 | 反向代理 |
|----------|------|----------|
| Nacos | `121.196.229.10:8848` | `nacos.kurimula-airi.top` |
| Sentinel | `121.196.229.10:7819` | `sentinel.kurimula-airi.top` |
| MySQL | `121.196.229.10:3306` | — |
| Nginx | `121.196.229.10:9080` | 反向代理到 Gateway |

只需将 4 个微服务打包为 Docker 镜像并在宿主机运行：

| 微服务 | 服务名 | 端口 | 容器名 |
|--------|--------|------|--------|
| Gateway | cr-gateway | 9999 | cr-gateway |
| Auth Service | cr-auth-service | 10002 | cr-auth-service |
| Business Service | cr-business-service | 10001 | cr-business-service |
| Admin Service | cr-admin-service | 10003 | cr-admin-service |

所有微服务使用 `network_mode: host`，直接访问宿主机网络。

### Jenkins CI/CD

Jenkins 运行在 Docker 中，通过 `pipeline/Jenkinsfile` 自动完成编译、构建镜像、部署。

**环境变量**：

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `NACOS_ADDR` | `nacos.kurimula-airi.top` | Nacos 地址 |
| `NACOS_NAMESPACE` | `course-record` | Nacos 命名空间 |
| `SENTINEL_ADDR` | `sentinel.kurimula-airi.top` | Sentinel 地址 |
| `DB_ADDR` | `121.196.229.10` | MySQL 地址 |

### Docker Compose（手动部署）

```bash
mvn clean package -DskipTests
docker compose up -d --build
docker compose ps
docker compose logs -f
```

### 本地开发

```powershell
$env:JAVA_HOME = "D:\JAVA\jdk\jdk21"
mvn clean package -DskipTests

# 确保 Nacos 配置中心已上传 nacos-config/ 下的配置文件
# 启动顺序：gateway → auth-service → business-service → admin-service
cd gateway && mvn spring-boot:run           # port 9999
cd auth-service && mvn spring-boot:run      # port 10002
cd business-service && mvn spring-boot:run  # port 10001
cd admin-service && mvn spring-boot:run     # port 10003
```
