# class_times_record

课时记录系统 —— 面向微信小程序的后端服务，涵盖课程、班级、学生、教师、机构管理等业务。

## 项目介绍

本项目为 uni-app 微信小程序提供后端服务，主要功能包括：

- **机构管理**：机构信息维护与查询
- **课程管理**：课程创建、模糊搜索、分享绑定
- **课时记录**：课时记录的增删改查、批量操作、分页加载
- **班级管理**：班级创建、班级与学生关联、班级人数自动统计
- **学生管理**：学生信息维护、按班级/教师维度查询
- **教师管理**：教师信息维护、按机构维度查询
- **家长关联**：家长与学生绑定、家长查询学生
- **权限管理**：菜单、权限、权限等级注入
- **认证鉴权**：微信登录（无密码/密码模式）、JWT 令牌认证、SM3 请求签名防篡改
- **数据加密**：基于国密 SM2 非对称加密、SM3 哈希算法的敏感数据保护

## 技术栈

| 类别     | 技术                        | 版本          |
|--------|---------------------------|-------------|
| 基础框架   | Spring Boot               | 4.0.4       |
| JDK    | Java 21（虚拟线程）             | 21          |
| ORM    | MyBatis-Plus              | 3.5.16      |
| 数据库    | MySQL                     | 8.0+        |
| 认证     | JWT (jjwt)                | 0.11.5      |
| API 文档 | SpringDoc OpenAPI         | 3.0.3       |
| 对象映射   | MapStruct                 | 1.5.5.Final |
| JSON   | Fastjson2                 | 2.0.60      |
| 加密     | BouncyCastle / 国密 SM2/SM3 | 1.84        |
| 工具     | Hutool                    | 5.8.40      |
| AOP    | Spring AOP (AspectJ)      | -           |
| 参数校验   | Hibernate Validator       | -           |
| 构建     | Maven                     | 3.9+        |

## 项目结构

```
class_times_record/
├── pipeline/
│   └── Jenkinsfile                        # CI/CD 流水线配置
├── .trae/
│   └── rules/
│       └── git-commit-message.md          # Git 提交规范
├── src/main/
│   ├── java/com/shiroko/
│   │   ├── ClassTimesRecordApplication.java  # Spring Boot 启动类
│   │   ├── annotation/                       # 自定义注解
│   │   │   └── UpdateStudentCount             # 标记触发班级人数更新
│   │   ├── aspect/                           # AOP 切面
│   │   │   └── StudentCountAspect             # 班级人数自动统计（AfterReturning）
│   │   ├── config/                           # 配置类
│   │   │   ├── MyBatisPlusConfig             # 分页插件、乐观锁、逻辑删除
│   │   │   ├── WebConfig                      # 三级拦截器注册
│   │   │   └── OpenApiConfig                  # SpringDoc 文档配置
│   │   ├── context/                          # 上下文工具
│   │   │   └── UserContext                    # ThreadLocal 用户上下文
│   │   ├── controller/                       # REST 控制器
│   │   │   ├── AuthController                # 认证：登录/注册/获取OpenId
│   │   │   ├── ClassController               # 班级管理
│   │   │   ├── CourseController              # 课程管理
│   │   │   ├── CourseRecordController        # 课时记录管理
│   │   │   ├── InstitutionController         # 机构管理
│   │   │   ├── MenuController                # 菜单管理
│   │   │   ├── PermissionRecordController    # 权限记录管理
│   │   │   ├── RecordController              # 记录管理
│   │   │   ├── StudentController             # 学生管理
│   │   │   ├── TeacherController             # 教师管理
│   │   │   └── TestController                # 测试接口
│   │   ├── converter/                        # MapStruct 对象转换器
│   │   │   ├── BaseConverter                 # 基础转换（含 LocalDateTime 转换）
│   │   │   ├── ClassConverter, CourseConverter, CourseRecordConverter
│   │   │   ├── MenuConverter, PermissionRecordConverter
│   │   │   ├── RecordConverter, StudentConverter
│   │   │   └── UserConverter
│   │   ├── exception/                        # 全局异常处理
│   │   │   ├── GlobalExceptionHandler        # 统一异常响应（参数校验/Runtime/SQL等）
│   │   │   └── CustomException               # 自定义业务异常
│   │   ├── filter/                           # Servlet 过滤器
│   │   │   └── RequestCachingFilter          # 请求体缓存（支持重复读取）
│   │   ├── interceptor/                      # Spring MVC 拦截器
│   │   │   ├── JwtInterceptor                # JWT 令牌认证
│   │   │   ├── SignInterceptor               # SM3 请求签名防篡改校验
│   │   │   └── UserInterceptor               # 用户上下文注入
│   │   ├── mapper/                           # MyBatis Mapper 接口
│   │   ├── repository/
│   │   │   ├── dto/                          # 请求 DTO（按业务模块分包）
│   │   │   │   ├── auth/                     # 登录/注册
│   │   │   │   ├── clazz/                    # 班级
│   │   │   │   ├── course/                   # 课程
│   │   │   │   ├── courserecord/             # 课时记录
│   │   │   │   ├── institution/              # 机构
│   │   │   │   ├── menu/                     # 菜单
│   │   │   │   ├── permissionrecord/         # 权限记录
│   │   │   │   ├── record/                   # 记录
│   │   │   │   ├── student/                  # 学生
│   │   │   │   └── teacher/                  # 教师
│   │   │   ├── entity/                       # 数据库实体
│   │   │   │   └── common/                   # BaseEntity、RoleBaseEntity
│   │   │   └── vo/                           # 响应 VO（按业务模块分包，与 DTO 对应）
│   │   ├── service/                          # 业务服务接口
│   │   │   └── impl/                         # 业务服务实现
│   │   ├── support/                          # 辅助类
│   │   │   └── RepeatedlyRequestWrapper      # 可重复读 HttpServletRequest 包装器
│   │   └── util/                             # 工具类
│   │       ├── JwtUtils                      # JWT 签发与校验（过期时间 5 分钟）
│   │       ├── SM2Util / SM2KeyGenerator     # SM2 国密非对称加密
│   │       └── SM3Util                       # SM3 国密哈希（用于签名校验）
│   └── resources/
│       ├── com/shiroko/mapper/               # MyBatis XML 映射文件
│       ├── application.yml                   # 主配置文件
│       ├── application-dev.yml               # 开发环境配置
│       ├── application-prod.yml              # 生产环境配置
│       ├── banner.txt                        # 启动 Banner
│       └── logback-spring.xml                # 日志配置
└── pom.xml                                   # Maven 项目配置
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.9+
- MySQL 8.0+

### 本地开发

1. 克隆项目

   ```bash
   git clone git@github.com:KurimulaAiri/class_times_record_back.git
   cd class_times_record
   ```

2. 配置数据库

    - 创建 MySQL 数据库 `class_times_record`
    - 修改 `application-dev.yml` 中的数据库连接信息

3. 启动应用

   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   应用默认运行在 `9999` 端口。

4. 访问 API 文档

    - Swagger UI: `http://localhost:9999/swagger-ui/index.html`
    - OpenAPI JSON: `http://localhost:9999/v3/api-docs`

### 构建部署

```bash
# 打包（跳过测试）
mvn clean package -DskipTests

# 生产环境启动
java -jar target/class_times_record-1.0-SNAPSHOT.jar --spring.profiles.active=prod
```

## 安全设计

### 三级防护体系

```
请求 → RequestCachingFilter → UserInterceptor → JwtInterceptor → SignInterceptor → Controller
```

#### 1. 用户上下文注入（UserInterceptor）

拦截所有请求，从 JWT 中提取用户身份信息（userId、roleType 等），注入到基于 `ThreadLocal` 的 `UserContext` 中，供后续业务层使用。

#### 2. JWT 令牌认证（JwtInterceptor）

校验请求头中的 JWT 令牌有效性与过期状态。**放行路径**：

- `/auth/**`（登录/注册/获取OpenId）
- `/v3/api-docs/**`、`/swagger-ui/**`（API 文档）
- `/test/**`（测试接口）
- `/error`（错误页）

JWT 过期时间：**5 分钟**。

#### 3. 请求签名校验（SignInterceptor）

基于 **SM3 国密哈希算法**实现请求签名防篡改与防重放攻击。

**签名流程**：

1. 前端在请求头中携带 `x-sign`（签名）、`x-timestamp`（时间戳）、`x-nonce`（随机数）
2. 服务端校验时间戳是否在 **60 秒**有效期内（防重放）
3. 收集所有请求参数（Query 参数 + Body JSON 参数 + 系统参数）
4. 按字典序排序后拼接为 `key=value&key=value` 格式
5. 追加盐值后使用 SM3 计算哈希，与客户端签名比对

**放行路径**：`/auth/**`、OpenAPI 文档、`/test/**`、`/error`

### 数据加密

- **SM2**：国密非对称加密算法，用于敏感数据（如密码）的加密存储与传输
- **SM3**：国密哈希算法，用于请求签名校验，替代传统的 MD5/SHA 方案

密钥配置位于 `application.yml` 中的 `crypto.sm2.private-key`。

## 核心机制

### 班级人数自动统计

通过 Spring AOP 实现班级学生人数的自动维护：

- `@UpdateStudentCount` 注解标记需要触发人数更新的 Service 方法
- `StudentCountAspect` 切面在方法成功返回后（`@AfterReturning`），从 `class_student`
  中间表统计当前班级的学生数量，自动更新到 `class` 表的 `student_count` 字段

### 虚拟线程

项目启用 JDK 21 虚拟线程（`spring.threads.virtual.enabled: true`），以轻量级线程模型提升高并发场景下的吞吐量，降低线程切换开销。

### 逻辑删除

所有实体通过 MyBatis-Plus 全局配置实现逻辑删除：

- `isDeleted = 0`：未删除
- `isDeleted = 1`：已删除

查询时自动过滤已删除数据，删除操作自动转为 `UPDATE`。

## 数据模型

| 领域 | 实体                                     | 说明             |
|----|----------------------------------------|----------------|
| 机构 | `Institution`                          | 机构信息           |
| 认证 | `User`、`UserAuth`                      | 用户与认证信息        |
| 课程 | `Course`、`CourseRecord`                | 课程与课时记录        |
| 班级 | `Class`、`ClassStudent`                 | 班级与班级学生关联（中间表） |
| 学生 | `Student`                              | 学生信息           |
| 教师 | `Teacher`                              | 教师信息           |
| 家长 | `Parent`、`ParentStudent`               | 家长与家长学生关联（中间表） |
| 权限 | `Permission`、`PermissionRecord`、`Menu` | 权限、权限记录、菜单     |
| 记录 | `Record`                               | 业务记录           |

**实体继承体系**：

- `BaseEntity`（基础实体）：`id`（雪花算法主键）、`createTime`、`updateTime`、`isDeleted`
- `RoleBaseEntity`（角色实体基类，继承 `BaseEntity`）：扩展角色相关公共字段

## CI/CD

项目使用 Jenkins Pipeline 进行持续集成与部署，配置文件位于 [pipeline/Jenkinsfile](pipeline/Jenkinsfile)。

| 阶段         | 操作                                                                 |
|------------|--------------------------------------------------------------------|
| **拉取代码**   | 从 GitHub `master` 分支通过 SSH 拉取                                      |
| **编译打包**   | `mvn clean package -DskipTests`，使用 JDK 21 + Maven 3.9.13           |
| **发布 jar** | 复制 jar 包到宿主机共享目录 `/opt/deploy/class_times_record_back`             |
| **停止旧进程**  | 通过 PID 文件 + 端口占用检查，kill 旧进程                                        |
| **启动新进程**  | `nohup java -jar` 以 `prod` profile 启动，轮询日志等待 `Started` 标识（最多 60 秒） |

部署目标：宿主机 `172.21.0.1:25184`，应用端口 `9999`。

## 开发规范

- 提交信息格式遵循 [Git 提交规范](.trae/rules/git-commit-message.md)
- 代码分层：`controller → service → mapper`，严格分离 DTO / Entity / VO
- 对象转换统一使用 MapStruct，位于 `converter/` 包
- 业务异常使用 `CustomException`，由 `GlobalExceptionHandler` 统一处理为 JSON 响应
- 所有实体使用逻辑删除，不进行物理删除
- 参数校验使用 Hibernate Validator 注解 + 分组校验
- 请求/响应数据包按业务模块分包管理（`dto/xxx/`、`vo/xxx/`）

## 相关文档

- [架构设计文档](docs/architecture.md) —— 系统架构、安全设计、数据库 ER 图、API 规范、CI/CD 等详细说明
