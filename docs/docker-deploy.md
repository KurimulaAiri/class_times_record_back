# class-times-record — Docker 容器化部署教程

## 一、前提条件

| 软件 | 最低版本 | 说明 |
|------|----------|------|
| Docker Desktop | 20.10+ | 容器引擎 + Compose (内置) |
| Java | JDK 21 | `D:\JAVA\jdk\jdk21` |
| Maven | 3.9+ | 仅用于编译 |

> **中国用户注意**：Docker Hub 在国内可能无法直接拉取镜像。如果遇到 `Image pull failed`，请参见 [附录 A](#附录-a中国用户镜像拉取)。

## 二、架构概览

```
                             ┌─────────────────┐
                             │     浏览器 / 客户端  │
                             └────────┬────────┘
                                      │ localhost:9080
                                      ▼
┌──────────────────────────────────────────────────────────────────┐
│                          docker compose                          │
│                                                                  │
│  ┌───────────────────────┐                                      │
│  │        Nginx          │ ← 统一入口，反向代理 (port 9080→80)    │
│  │       :80 (内部)       │                                      │
│  └───────────┬───────────┘                                      │
│              │  proxy_pass                                      │
│              ▼                                                   │
│  ┌───────────────────────┐                                      │
│  │       Gateway          │ ← API 网关 + 鉴权 (port 9999)        │
│  │  lb:// 路由转发         │                                      │
│  └───┬───────────────┬───┘                                      │
│      │               │                                           │
│      ▼               ▼                                           │
│  ┌──────────┐  ┌──────────────┐    ┌──────────┐ ┌──────────┐  │
│  │  Auth    │  │  Business    │    │  Nacos   │ │  MySQL   │  │
│  │ Service  │  │  Service     │    │  :8848   │ │  :3306   │  │
│  │ :10002   │  │  :10001      │    │          │ │          │  │
│  └──────────┘  └──────────────┘    └──────────┘ └──────────┘  │
│                                                                  │
│  app-network (bridge) — 容器间通过服务名互相访问                   │
└──────────────────────────────────────────────────────────────────┘
```

## 三、文件结构

```
backend/
├── docker-compose.yml          # 容器编排文件
├── docker/nginx/
│   ├── nginx.conf               # Nginx 主配置
│   └── conf.d/default.conf      # 反向代理站点配置
├── gateway/
│   ├── Dockerfile              # Gateway 镜像
│   ├── pom.xml                 # 含 spring-cloud-starter-loadbalancer 依赖
│   └── src/.../config/RouteConfig.java  # 编程式路由定义
├── auth-service/Dockerfile     # Auth Service 镜像
├── business-service/Dockerfile # Business Service 镜像
├── docs/
│   ├── class_times_record.sql  # 数据库初始化脚本
│   └── docker-deploy.md        # 本文档
└── .dockerignore
```

## 四、快速启动

### 4.1 第一步：编译项目

```powershell
# 进入项目目录，设置 JDK 并编译
cd d:\PRJ\fully-function-project\course_record\backend
$env:JAVA_HOME = "D:\JAVA\jdk\jdk21"
mvn clean package -DskipTests
```

编译完成后确认 jar 包已生成：

```
gateway/target/gateway-1.0-SNAPSHOT.jar
auth-service/target/auth-service-1.0-SNAPSHOT.jar
business-service/target/business-service-1.0-SNAPSHOT.jar
```

### 4.2 第二步：启动所有服务

```powershell
docker compose up -d --build
```

`--build` 会强制重建三个微服务镜像，确保使用最新 jar 包。

### 4.3 第三步：查看启动状态

```powershell
# 查看所有容器状态（等待 Nacos 启动约需 60-90 秒）
docker compose ps

# 查看实时日志
docker compose logs -f

# 只查看特定服务的日志
docker compose logs -f gateway
```

### 4.4 第四步：验证服务

#### 检查 Nacos 注册中心

浏览器打开 [http://localhost:8848/nacos/](http://localhost:8848/nacos/)，无需登录（认证已关闭），进入「服务管理 → 服务列表」：

| 服务名 | 实例数 |
|--------|--------|
| `gateway` | 1 |
| `auth-service` | 1 |
| `business-service` | 1 |

也可以通过 API 验证：

```powershell
Invoke-RestMethod "http://localhost:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=20"
```

#### 测试 API 端点

```powershell
# Nginx 健康检查（直接验证反向代理可用）
Invoke-RestMethod http://localhost:9080/nginx-health

# 通过 Nginx 访问 Gateway 健康检查
Invoke-RestMethod http://localhost:9080/actuator/health

# 通过 Nginx → Gateway → Auth Service 获取 OpenId
Invoke-RestMethod -Method POST `
  -Uri http://localhost:9080/auth/auth/get_open_id `
  -Body '{"code":"test_wx_code"}' `
  -ContentType "application/json"
# 预期：{"code":400,"message":"微信登录失败: invalid code..."} → 全链路通

# 直连 Gateway（跳过 Nginx）也可用
Invoke-RestMethod http://localhost:9999/actuator/health
```

## 五、服务端口与容器名

| 服务 | 容器名 | 宿主机端口 | 说明 |
|------|--------|------------|------|
| **Nginx** | `cr-nginx` | **9080** | **统一入口，反向代理到 Gateway** |
| Nacos | `cr-nacos` | 8848, 9848 | NACOS_AUTH_ENABLE=false (免登录) |
| MySQL | `cr-mysql` | 3306 | utf8mb4 字符集 |
| Gateway | `cr-gateway` | 9999 | Spring Cloud Gateway + LoadBalancer |
| Auth Service | `cr-auth-service` | 10002 | 认证授权模块 |
| Business Service | `cr-business-service` | 10001 | 核心业务模块 |
| Sentinel | `cr-sentinel` | 8080 | 流量控制面板 (可选) |

> **开发接入点**：统一通过 Nginx `localhost:9080` 访问所有 API。Gateway `localhost:9999` 也可直连（跳过 Nginx）。 |

## 六、常用操作

### 6.1 查看日志

```powershell
docker compose logs -f                  # 全部实时日志
docker compose logs --tail=200 gateway  # gateway 最近 200 行
```

### 6.2 重启单个服务

```powershell
# 修改代码后重新打包并部署单个服务
mvn package -pl auth-service -am -DskipTests -q
docker compose up -d --build auth-service
```

### 6.3 停止所有服务

```powershell
docker compose down             # 停止但保留数据卷
docker compose down -v          # 停止并删除数据卷（⚠ 清空数据库和 Nacos 配置）
```

### 6.4 进入容器调试

```powershell
docker exec -it cr-auth-service sh
docker exec -it cr-mysql bash
```

### 6.5 容器间网络测试

```powershell
# 从 gateway 容器直接访问 auth-service
docker exec cr-gateway wget -q -O- http://auth-service:10002/actuator/health
```

### 6.6 查看资源占用

```powershell
docker stats
```

## 七、环境变量说明

| 环境变量 | 对应 Spring 属性 | 容器中值 | 说明 |
|----------|------------------|----------|------|
| `NACOS_SERVER_ADDR` | `nacos-server-addr` | `nacos:8848` | Nacos 地址（容器间通过服务名通信） |
| `DATABASE_ADDRESS` | `database-address` | `mysql` | MySQL 地址（容器间通过服务名通信） |

## 八、数据持久化

| 数据卷 | 内容 |
|--------|------|
| `mysql_data` | 数据库文件 |
| `nacos_data` | Nacos 配置快照 |
| `nacos_logs` | Nacos 运行日志 |

### 数据库备份与恢复

```powershell
# 导出
docker exec cr-mysql mysqldump -u root -proot123456 class_times_record > backup.sql

# 恢复
docker exec -i cr-mysql mysql -u root -proot123456 class_times_record < backup.sql
```

## 九、故障排查

### 9.1 Gateway 路由返回 404

**症状**：`docker compose ps` 显示所有容器健康，但通过 Gateway 访问返回 404。

**根因**：Gateway 的 YAML 路由配置 (`application.yml` 中的 `spring.cloud.gateway.routes`) 在容器化启动时可能未正确加载（routes count = 0）。

**解决**：项目已内置编程式路由配置 [RouteConfig.java](../gateway/src/main/java/com/shiroko/gateway/config/RouteConfig.java)，确保路由在任何环境下都能正确注册：

```java
@Configuration
public class RouteConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://auth-service"))
                .route("business-service", r -> r.path("/biz/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://business-service"))
                .build();
    }
}
```

### 9.2 lb:// 路由返回 503

**症状**：路由匹配成功但返回 `503 Service Unavailable`，日志中出现 `NoLoadBalancerClientFilter`。

**根因**：Gateway 缺少 `spring-cloud-starter-loadbalancer` 依赖。

**解决**：已在 `gateway/pom.xml` 中添加该依赖，重新编译即可。

### 9.3 Nacos 容器崩溃

**症状**：Nacos 日志报 `JWT HMAC-SHA algorithm ... key byte array is 0 bits`。

**根因**：`NACOS_AUTH_ENABLE=true` 需要配置 JWT 密钥，未配置会导致初始化失败。

**解决**：在 `docker-compose.yml` 中设置 `NACOS_AUTH_ENABLE: "false"`（开发环境关闭认证）。

### 9.4 容器间网络不通

```powershell
docker network inspect backend_app-network
docker exec cr-gateway wget -q -O- http://auth-service:10002/actuator/health
```

### 9.5 Nacos 中看不到注册的服务

等待 Nacos 完全启动后再启动微服务（通常需要 60-90 秒）。也可以重启微服务触发重新注册：

```powershell
docker compose restart auth-service business-service gateway
```

## 十、生产环境建议

1. **安全加固** — 修改默认密码，启用 Nacos Auth，使用 Docker Secrets
2. **高可用** — Nacos 集群 + MySQL 主从 + 微服务多实例
3. **监控** — Sentinel 规则持久化到 Nacos，接入 Prometheus + Grafana
4. **CI/CD** — 将 `mvn package` + `docker compose up -d --build` 整合到流水线

## 十一、完整启动流程

```
┌─────────┐
│ 1. 编译  │  mvn clean package -DskipTests
└────┬────┘
     ▼
┌─────────┐
│ 2. 构建  │  docker compose build
└────┬────┘
     ▼
┌─────────────┐     ┌──────────┐
│ 3. 启动全部  │ ──→ │ Nacos    │ (standalone, auth=off)
│ docker compose│     │ MySQL    │ (初始化 class_times_record)
│ up -d --build│     └────┬─────┘
└─────────────┘          │ 等待 Nacos+MySQL 就绪
                         ▼
                   ┌──────────────┐
                   │ 微服务依次启动  │
                   │ Gateway       │ 注册到 Nacos
                   │ Auth Service  │ 通过 lb:// 互相发现
                   │ Biz Service   │
                   └──────┬───────┘
                          │ 等待 Gateway 上线
                          ▼
                   ┌──────────────┐
                   │ Nginx 启动    │ ← 最后启动（依赖 Gateway）
                   │ 监听 9080     │
                   └──────┬───────┘
                          ▼
                   ┌──────────────┐
                   │ ✅ 全部就绪    │
                   │ 访问 9080 端口 │
                   └──────────────┘
```

---

## 附录 A：中国用户镜像拉取

国内网络环境可能无法直接拉取 Docker Hub 镜像。以下两种方案任选其一：

### 方案一：配置 Docker 镜像加速器

编辑 `%USERPROFILE%\.docker\daemon.json`：

```json
{
  "registry-mirrors": [
    "https://docker.1panel.live"
  ]
}
```

重启 Docker Desktop 后生效。

### 方案二：通过 DaoCloud 手动拉取并打标签

```powershell
# 拉取基础镜像（四个命令可并行执行）
docker pull docker.m.daocloud.io/library/mysql:8.0
docker pull docker.m.daocloud.io/nacos/nacos-server:v2.3.2
docker pull docker.m.daocloud.io/library/eclipse-temurin:21-jre-alpine
docker pull docker.m.daocloud.io/library/nginx:alpine

# 打标签为 docker-compose.yml 中引用的镜像名
docker tag docker.m.daocloud.io/library/mysql:8.0 mysql:8.0
docker tag docker.m.daocloud.io/nacos/nacos-server:v2.3.2 nacos/nacos-server:v2.3.2
docker tag docker.m.daocloud.io/library/eclipse-temurin:21-jre-alpine eclipse-temurin:21-jre-alpine
docker tag docker.m.daocloud.io/library/nginx:alpine nginx:alpine

# 后续正常启动
docker compose up --detach --build
```
