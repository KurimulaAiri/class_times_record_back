# AGENTS.md — 教学录播课表记录后端

基于 Spring Cloud Alibaba 微服务架构的教学管理系统后端。

## 项目结构

```
backend/
├── common/            # 共享代码库：DTO、VO、Entity、Service 接口、工具类
├── docker/nginx/      # Nginx 配置（统一反向代理入口）
├── gateway/           # Spring Cloud Gateway (端口 9999)
├── auth-service/      # 认证授权微服务 (端口 10002)
├── business-service/  # 核心业务微服务 (端口 10001)
├── docs/              # 文档和初始化 SQL
├── docker-compose.yml # 容器编排文件
└── AGENTS.md          # 本文件
```

## 技术栈

| 组件 | 技术 |
|------|------|
| 语言 | Java 21 |
| 框架 | Spring Boot 3.5, Spring Cloud 2024 |
| 微服务治理 | Spring Cloud Gateway + Alibaba Nacos + Sentinel |
| 数据访问 | MyBatis-Plus + MySQL |
| 构建工具 | Maven 多模块 |
| 容器化 | Docker + Docker Compose |

## 目录约定

- `gateway/src/main/java/com/shiroko/gateway/` — Gateway 应用入口和配置
- `auth-service/src/main/java/com/shiroko/` — Auth Service 应用入口和业务
- `business-service/src/main/java/com/shiroko/` — Business Service 应用入口和业务
- `common/src/main/java/com/shiroko/` — 跨模块共享代码

## 核心模块

### common

- `converter/` — MapStruct 转换器接口
- `repository/dto/` — 数据传输对象
- `repository/entity/` — MyBatis-Plus 实体类
- `repository/input/` — 请求输入对象
- `repository/vo/` — 视图对象（返回给前端）
- `service/` — 业务 Service 接口
- `utils/` — 工具类 (JwtUtils, SnowflakeIdWorker 等)
- `constant/` — 常量定义
- `enums/` — 枚举
- `interceptor/` — 通用拦截器
- `context/` — 线程上下文

### gateway

- `config/` — Gateway 配置（包括编程式路由定义 RouteConfig）
- `filter/` — 自定义过滤器 (JwtAuthFilter)

### auth-service

- 认证相关接口（微信登录、Token 刷新等）
- 自定义 `SignInterceptor` 签名验证拦截器

### business-service

- 机构管理、教职人员、课程、学生等核心业务
- 自定义 `SignInterceptor` 签名验证拦截器

## 跨模块约定

### DTO / VO / Entity / Input / Converter

- `repository/dto/` 下的 DTO 类使用 `@Data` 注解
- `repository/vo/` 下的 VO 类使用 `@Data` 注解，返回前端
- `repository/entity/` 下的实体类继承 `BaseEntity`（含创建/更新时间），使用 `@TableName` 指定表名
- `repository/input/` 下的 Input 类表示请求参数
- `converter/` 下的 Converter 接口使用 `@Mapper(componentModel = "spring")`

### Service

- Service 接口定义在 `common/src/.../service/` 中
- ServiceImpl 实现在各微服务模块中（`auth-service` 或 `business-service`）
- 使用 `@Service` 注解标记实现类

### Controller

- 使用 `@RestController` + `@RequestMapping`
- 统一返回 `ResponseDTO<T>` 封装
- Auth Service 的 Controller 路径前缀通常在 Gateway 中通过路由映射

### Gateway 路由

路由通过编程式 Java 配置定义（`gateway/src/.../config/RouteConfig.java`），而非 YAML 配置：

```java
// /auth/** → StripPrefix=1 → lb://auth-service
// /biz/**  → StripPrefix=1 → lb://business-service
```

Gateway 依赖 `spring-cloud-starter-loadbalancer` 实现 `lb://` 服务发现。

### 安全

- `JwtAuthFilter` (Gateway) 在各路由请求进入前校验 JWT Token
- `SignInterceptor` (auth-service, business-service) 校验 API 签名参数
- `UserContext` (ThreadLocal, servlet services only) for per-request user identity
- `JwtUtils` supports both AccessToken and RefreshToken

## Adding a New Feature

1. **Determine domain**: auth (认证授权) or business (核心业务)
2. **Add entity** in `common/src/.../repository/entity/`
3. **Add DTOs/VOs** in `common/src/.../repository/dto/` and `vo/`
4. **Add converter** in `common/src/.../converter/`
5. **Add Service interface** in `common/src/.../service/`
6. **Add Mapper interface + XML** in the appropriate service module
7. **Add ServiceImpl** in the appropriate service module
8. **Add Controller** in the appropriate service module
9. **Add route** in `gateway/src/.../config/RouteConfig.java` if new path prefix needed

## Building & Running

### Docker Compose（推荐）

一键启动全部服务（Nginx + Nacos + MySQL + 微服务 + Sentinel）：

```powershell
cd d:\PRJ\fully-function-project\course_record\backend

# 1. 编译
$env:JAVA_HOME = "D:\JAVA\jdk\jdk21"
mvn clean package -DskipTests

# 2. 启动（首次启动自动构建镜像）
docker compose up -d --build

# 3. 查看状态
docker compose ps
docker compose logs -f
```

**服务访问地址**：

| 服务 | 地址 | 说明 |
|------|------|------|
| **Nginx (统一入口)** | **http://localhost:9080** | 反向代理到 Gateway |
| Nacos 控制台 | http://localhost:8848/nacos/ | 免登录 (auth=off) |
| MySQL | localhost:3306 | root/root123456 |
| Gateway (直连) | http://localhost:9999 | 跳过 Nginx 直接访问 |
| Sentinel 面板 | http://localhost:8080 | sentinel/sentinel |

**常用命令**：

```powershell
docker compose down                        # 停止所有服务（保留数据）
docker compose down -v                     # 停止并删除数据卷（⚠ 清空数据库）
docker compose restart auth-service        # 重启单个服务
docker compose logs -f nginx               # 查看 Nginx 日志
docker compose up -d --build auth-service  # 重新构建并启动单个服务
```

**API 验证**：

```powershell
# Nginx 健康检查
Invoke-RestMethod http://localhost:9080/nginx-health

# 通过 Nginx → Gateway 健康检查
Invoke-RestMethod http://localhost:9080/actuator/health

# 全链路测试：Nginx → Gateway → Auth Service
Invoke-RestMethod -Method POST -Uri http://localhost:9080/auth/auth/get_open_id `
  -Body '{"code":"test"}' -ContentType "application/json"
```

**容器列表**：

| 服务 | 容器名 |
|------|--------|
| Nginx | `cr-nginx` |
| Nacos | `cr-nacos` |
| MySQL | `cr-mysql` |
| Gateway | `cr-gateway` |
| Auth Service | `cr-auth-service` |
| Business Service | `cr-business-service` |
| Sentinel | `cr-sentinel` |

> 完整教程见 [docs/docker-deploy.md](docs/docker-deploy.md)

### 本地开发（直接运行）

```powershell
$env:JAVA_HOME = "D:\JAVA\jdk\jdk21"

# Build all modules
mvn clean package -DskipTests

# Run services (start Nacos first!)
# Nacos: startup.cmd -m standalone  (from nacos/bin/)
# Sentinel Dashboard: java -jar sentinel-dashboard.jar (port 8080)

# Start services in order:
cd gateway && mvn spring-boot:run           # port 9999
cd auth-service && mvn spring-boot:run      # port 10002
cd business-service && mvn spring-boot:run  # port 10001
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
