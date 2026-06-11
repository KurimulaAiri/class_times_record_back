# class-times-record 架构设计文档

## 一、系统架构概览

### 1.1 项目定位

class-times-record 是一个面向 uni-app 微信小程序的后端服务系统，提供课时记录、班级管理、学生教师管理等核心业务能力。采用 **Spring Cloud Alibaba 微服务架构**。

### 1.2 技术架构层级

```
┌─────────────────────────────────────────────────────────┐
│                   微信小程序 (uni-app)                     │
└───────────────────────────┬─────────────────────────────┘
                            │ HTTPS + JWT + SM3 签名
┌───────────────────────────▼─────────────────────────────┐
│              Spring Cloud Gateway (:9999)                 │
│  ┌──────────────────────────────────────────────────┐   │
│  │  JwtAuthFilter (全局过滤器, Order=-100)              │   │
│  │  - 公开路径放行 (登录/注册/Swagger)                    │   │
│  │  - JWT 校验 → X-User-Id / X-User-Role 请求头注入     │   │
│  │  - Sentinel 流量控制 / 熔断降级                        │   │
│  └──────────────────┬───────────────────────────────┘   │
│                     │ Nacos 服务发现 (lb://)              │
│         ┌───────────┼──────────────┐                    │
│         ▼           ▼              ▼                    │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐               │
│  │ auth-    │ │ business-│ │ Nacos    │               │
│  │ service  │ │ service  │ │ (:8848)  │               │
│  │(:10002)  │ │(:10001)  │ │ 注册中心  │               │
│  └────┬─────┘ └────┬─────┘ └──────────┘               │
│       │             │                                   │
│       └──────┬──────┘                                   │
│              ▼                                          │
│  ┌──────────────────────────────────────────────────┐   │
│  │            common 共享模块                          │   │
│  │  Entity / DTO / VO / Converter / Service 接口      │   │
│  │  Filter / Interceptor / Util / Config             │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                            │
                    ┌───────▼───────┐
                    │  MySQL 8.0+   │
                    └───────────────┘
```

### 1.3 服务端口与职责

| 服务 | 端口 | 职责 |
|------|------|------|
| Gateway | 9999 | 统一入口，路由分发，JWT 验证 |
| **auth-service** | 10002 | **认证授权域**：登录/注册/Token、用户管理、角色权限(RBAC)、菜单管理、权限记录 |
| **business-service** | 10001 | **核心业务域**：机构、学生、教师、家长、班级、排课、课程、课程记录、课时记录 |
| Nacos Server | 8848 | 服务注册与发现 |
| Sentinel Dashboard | 8080 | 流量控制面板 |

> 两个服务各司其职，不再镜像部署。business-service 保留少量 auth 域 Mapper（UserMapper、PermissionRecordMapper）以满足 TeacherServiceImpl 和 CourseRecordServiceImpl 的跨域数据关联。

### 1.4 路由规则

| 外部路径 | 目标服务 | StripPrefix | 覆盖控制器 |
|----------|----------|-------------|------------|
| `/auth/**` | `lb://auth-service` | 1 | AuthController、MenuController、PermissionRecordController |
| `/biz/**` | `lb://business-service` | 1 | Institution、Student、Teacher、Class、Course、CourseRecord、Record、ClassSchedule |

### 1.5 技术栈明细

| 层级 | 技术 | 版本 | 用途 |
|------|------|------|------|
| 基础框架 | Spring Boot | 4.0.4 | 应用框架 |
| JDK | Java | 21 | 运行环境（启用虚拟线程） |
| 微服务 | Spring Cloud Alibaba | 2025.1.0.0 | Nacos + Sentinel |
| 微服务基座 | Spring Cloud | 2025.1.1 | Gateway + LoadBalancer |
| 网关 | Spring Cloud Gateway | (Cloud BOM) | 响应式网关 |
| 注册中心 | Nacos | (Alibaba BOM) | 服务注册与发现 |
| 流量控制 | Sentinel | (Alibaba BOM) | 限流 / 熔断 |
| ORM | MyBatis-Plus | 3.5.16 | 数据持久层 |
| 数据库 | MySQL | 8.0+ | 关系型数据库 |
| 认证 | jjwt | 0.11.5 | JWT 令牌签发与校验 |
| API 文档 | SpringDoc OpenAPI | 3.0.3 | Swagger UI + OpenAPI 规范 |
| 对象映射 | MapStruct | 1.5.5.Final | DTO ↔ Entity ↔ VO 转换 |
| JSON | Fastjson2 | 2.0.60 | JSON 序列化/反序列化 |
| 加密 | BouncyCastle | 1.84 | SM2/SM3 国密算法 |
| 工具 | Hutool | 5.8.40 | 通用工具类 |
| ID 混淆 | hashids | 1.0.3 | 机构码生成 |
| 参数校验 | Hibernate Validator | - | Bean Validation (JSR-303) |
| AOP | Spring AOP | - | 班级人数自动统计切面 |
| 构建 | Maven | 3.9+ | 项目构建与依赖管理 |

---

## 二、安全设计

### 2.1 两级防护体系

```
外部请求 → Gateway (:9999)
  │
  ▼
┌──────────────────────────────────────┐
│ Level 1: Gateway JwtAuthFilter       │
│ (GlobalFilter, Order = -100)         │
│                                      │
│ 公开路径 (跳过 JWT):                   │
│   /auth/auth/login_no_pwd            │
│   /auth/auth/login_by_pwd            │
│   /auth/auth/login_by_token          │
│   /auth/auth/get_open_id             │
│   /auth/auth/register                │
│   /auth/auth/refresh                 │
│   /biz/institution/get_by_open_id    │
│   /biz/institution/get_by_institution_code │
│   Swagger / api-docs 路径             │
│                                      │
│ 其他路径: 校验 Bearer Token            │
│   → 提取 userId, roleId              │
│   → 注入 X-User-Id, X-User-Role 请求头 │
└──────────────┬───────────────────────┘
               ▼
         Nacos 负载均衡
               ▼
┌──────────────────────────────────────┐
│ Level 2: Service 拦截器链             │
│ (auth-service / business-service)    │
│                                      │
│ RequestCachingFilter                 │
│   → 缓存 Body 支持重复读取             │
│                                      │
│ GatewayUserFilter                    │
│   → 读取 X-User-Id/X-User-Role       │
│   → 写入 UserContext (ThreadLocal)    │
│                                      │
│ UserInterceptor                      │
│   → 从 JWT Token 提取用户信息          │
│                                      │
│ JwtInterceptor                       │
│   → 二次校验 JWT 有效性                │
│   (跳过: /auth/**, /swagger-ui/**,    │
│    /v3/api-docs/**, /test/**, /error) │
│                                      │
│ SignInterceptor                      │
│   → SM3 请求签名校验 + 防重放           │
│   (跳过同上)                           │
└──────────────┬───────────────────────┘
               ▼
           Controller
```

### 2.2 签名机制详解

**请求头携带参数**：

| 请求头 | 说明 |
|--------|------|
| `x-sign` | SM3 签名哈希值 |
| `x-timestamp` | Unix 时间戳（秒），有效期 60s |
| `x-nonce` | 随机数 |

**签名算法流程**：

1. 收集 Query 参数与 Body JSON 参数（递归展平）
2. 将所有参数按 Key 字典序升序排列
3. 拼接为 `a=1&b=2&...&z=n` 格式
4. 追加系统盐值（配置在 `application.yml`）
5. 使用 SM3 算法计算哈希
6. 与客户端 `x-sign` 比较

### 2.3 数据加密

| 算法 | 类型 | 用途 |
|------|------|------|
| SM2 | 非对称加密 | 敏感数据传输加密（密码等） |
| SM3 | 哈希算法 | 密码摘要、请求签名校验 |

**密钥管理**：SM2 私钥配置在 `crypto.sm2.private-key` 中，不同环境可配置不同密钥。

### 2.4 JWT 双 Token 机制

`JwtUtils` 支持两种 Token：

| Token 类型 | 过期时间 | 用途 |
|------------|----------|------|
| AccessToken | 24h（`jwt.expiration`） | 业务请求鉴权 |
| RefreshToken | 更长 | 用于刷新 AccessToken（`/auth/auth/refresh`） |

---

## 三、核心机制

### 3.1 班级人数自动统计 (AOP)

**触发条件**：Service 方法标注 `@UpdateStudentCount` 注解

**执行流程**：

```
Service 方法执行成功
  │
  ▼
@AfterReturning 切面触发
  │
  ▼
StudentCountAspect
  │
  ├─ 从方法参数中解析 classId
  ├─ 查询 class_student 中间表 COUNT(class_id)
  ├─ 更新 class 表 student_count 字段
  └─ 结束
```

**使用场景**：

- 班级添加/移除学生
- 学生退班

### 3.2 逻辑删除

通过 MyBatis-Plus 全局配置实现：

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: isDeleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

**行为**：

- 查询时自动追加 `WHERE is_deleted = 0`
- 删除时自动转为 `UPDATE ... SET is_deleted = 1`

### 3.3 虚拟线程

应用启用 JDK 21 虚拟线程（配置 `spring.threads.virtual.enabled: true`），以轻量级用户态线程替代传统平台线程：

- 每个请求绑定一个虚拟线程，无池化开销
- I/O 阻塞时自动挂起并释放底层载体线程
- 适合本项目的 I/O 密集型场景（数据库查询、外部 API 调用）

### 3.4 主键策略

全局使用 MyBatis-Plus 雪花算法（`IdType.ASSIGN_ID`）生成主键：

- 分布式唯一 ID
- 趋势递增，利于 MySQL 索引
- 无需中心化 ID 生成服务

### 3.5 统一异常处理

`GlobalExceptionHandler` 统一拦截以下异常：

| 异常类型 | 处理方式 |
|----------|----------|
| `BindException` / `MethodArgumentNotValidException` | 参数校验失败，返回具体字段错误信息 |
| `BusinessException` | 自定义业务异常，返回异常定义的 code 与 message |
| `IllegalContentException` | 内容违规异常 |
| `RuntimeException` | 运行时异常，兜底处理 |
| `SQLException` | 数据库异常，返回通用错误 |
| `Exception` | 未知异常，兜底处理 |

---

## 四、项目分层规范

### 4.1 模块职责

| 模块 | 职责 | 说明 |
|------|------|------|
| `common` | 共享库 | Entity、DTO、VO、Converter、Service 接口、Filter、Interceptor、Config、Util |
| `gateway` | API 网关 | 路由分发、JWT 校验、Sentinel 流控、CORS |
| `auth-service` | 认证授权服务 | AuthController / MenuController / PermissionRecordController + 认证相关 ServiceImpl（Nacos 注册，端口 10002） |
| `business-service` | 核心业务服务 | Institution / Student / Teacher / Class / Course / CourseRecord / Record / ClassSchedule Controller + 业务 ServiceImpl（Nacos 注册，端口 10001） |

### 4.2 目录与包职责

| 包路径 | 职责 | 说明 |
|--------|------|------|
| `controller/` | 接入层 | 接收请求、参数校验、调用 Service、组装响应 |
| `service/` | 业务层接口（common） | 定义业务逻辑契约 |
| `service/impl/` | 业务层实现（auth/business） | 实现业务逻辑，调用 Mapper |
| `mapper/` | 持久层接口（auth/business） | MyBatis-Plus BaseMapper 扩展 |
| `repository/entity/` | 数据库实体（common） | 与表结构一一对应 |
| `repository/dto/` | 请求对象（common） | 按业务模块分包，含校验注解 |
| `repository/vo/` | 响应对象（common） | 按业务模块分包，可嵌套其他 VO |
| `converter/` | 对象转换（common） | MapStruct 自动生成转换代码 |
| `config/` | Spring 配置（common） | MyBatis-Plus / Web / OpenAPI 配置 |
| `interceptor/` | 拦截器（common） | 认证、签名、上下文注入 |
| `filter/` | 过滤器（common） | 请求体缓存、用户上下文注入 |
| `aspect/` | AOP 切面（common） | 班级人数自动统计 |
| `exception/` | 异常处理（common） | 全局异常处理器 + 自定义异常 |
| `util/` | 工具类（common） | JWT / SM2 / SM3 / DateTransform / InstitutionCode |
| `annotation/` | 自定义注解（common） | `@UpdateStudentCount` 等 |
| `context/` | 上下文（common） | `UserContext` (ThreadLocal) |
| `support/` | 辅助类（common） | 请求包装器 |

### 4.3 数据流转规范

```
请求进入
  │
  ├─ Controller: 接收 DTO，调用 Service
  │     │
  │     └─ Service: DTO → (MapStruct) → Entity，调用 Mapper
  │           │
  │           └─ Mapper: Entity ↔ 数据库
  │
  └─ Controller: Service 返回 VO，封装 ResponseDTO<T>
```

- **入参**：统一使用 `@RequestBody` DTO，禁止使用基本类型散参
- **出参**：统一使用 `ResponseDTO<T>` 包装，`T` 为具体 VO 类型
- **跨层转换**：统一使用 MapStruct（位于 `common/converter/` 包）
- **HTTP 方法**：所有业务接口统一使用 POST（即使在语义上是查询操作）

### 4.4 参数校验

- 使用 Hibernate Validator 注解（`@NotNull`、`@NotBlank`、`@Valid` 等）
- 支持分组校验（`@Validated(Group.class)`）
- 校验分组定义在各自 DTO 包的 `validategroup/` 子包中
- 校验失败由 `GlobalExceptionHandler` 统一返回 `ResponseDTO`

---

## 五、数据库设计

### 5.1 ER 关系总览

```
                         ┌──────────┐
                         │  user_auth│
                         │ (认证表)  │
                         └─────┬────┘
                   user_id│     │role_id
         ┌────────────────┘     └──────────────┐
         ▼                                     ▼
    ┌─────────┐                         ┌────────────┐
    │  user   │                         │ permission  │
    │ (用户表)│                         │  (权限/角色) │
    └────┬────┘                         └──────┬─────┘
         │user_id                    permission_id│
    ┌────┼────────────┐                    ┌────┴─────┐
    ▼    ▼            ▼                    │ role_menu│
┌───────┐ ┌───────┐  ┌──────────────┐      └────┬─────┘
│teacher│ │parent │  │permission    │      menu_id│
│(教师) │ │(家长) │  │_record       │           ▼
└───┬───┘ └──┬────┘  │(权限记录)     │     ┌─────────┐
    │        │       └──────┬───────┘     │  menu   │
    │   ┌────┴─────┐        │             │ (菜单表) │
    │   │parent     │  course_record_id    └─────────┘
    │   │_student   │        │
    │   │(家长-学生) │  ┌─────┴──────┐
    │   └────┬──────┘  ▼            ▼
    │        │     ┌──────────────┐  ┌────────┐
    │   student_id │course_record │  │ record │
    │        │     │(课程记录/购课)│◄─┤(课时记录)│
    │        ▼     └──────┬───────┘  └────────┘
    │   ┌─────────┐       │
    │   │ student │  student_id, course_id
    │   │ (学生)  │       │
    │   └────┬────┘  ┌────┴─────┐
    │  class_id│     ▼          ▼
    │   ┌─────┴──────┐  ┌───────────┐
    │   │class_student│  │  course   │
    │   │(班级-学生)  │  │  (课程)   │
    │   └─────┬──────┘  └─────┬─────┘
    │    class_id        course_id│
    │        ▼                   ▼
    │   ┌─────────┐         ┌─────────┐
    └──►│  class  │         │teacher  │
  teacher│ (班级)  │         │_course  │──┐
   _id   └─────────┘         │(教师-课程)│ │
                             └─────────┘  │
                                  teacher_id│
                                           ▼
                                       ┌───────┐
                                       │teacher│
                                       └───────┘

institution ──1:N──> teacher
institution ──1:N──> student
institution ──1:N──> course
```

### 5.2 表清单

| 分类 | 表名 | 实体类 | 说明 |
|------|------|--------|------|
| 基础 | `user` | `User` | 用户基础信息(手机号/openid/unionId) |
| 认证 | `user_auth` | `UserAuth` | 登录凭据(账号/密码/盐值/角色) |
| 平台 | `user_platform` | `UserPlatform` | 多平台用户关联 |
| 管理员 | `admin` | `Admin` | 管理员信息 |
| 权限 | `permission` | `Permission` | 角色定义(名称/权重) |
| 菜单 | `menu` | `Menu` | 前端菜单配置(图标/路径/排序) |
| 机构 | `institution` | `Institution` | 培训机构信息 |
| 教师 | `teacher` | `Teacher` | 教师信息 |
| 家长 | `parent` | `Parent` | 家长信息 |
| 学生 | `student` | `Student` | 学生信息(姓名/性别/学校等) |
| 课程 | `course` | `Course` | 课程信息(按次/按天) |
| 班级 | `class` | `Class` | 班级信息(人数/上限) |
| 班级排课 | `class_schedule` | `ClassSchedule` | 班级上课时间表 |
| 课程记录 | `course_record` | `CourseRecord` | 学生购课记录(总量/剩余/状态) |
| 课时记录 | `record` | `Record` | 课时变更明细(增/减) |
| 权限记录 | `permission_record` | `PermissionRecord` | 课程记录-用户权限关联 |
| 中间表 | `class_student` | `ClassStudent` | 班级-学生 N:M 关联 |
| 中间表 | `parent_student` | `ParentStudent` | 家长-学生 N:M 关联 |
| 中间表 | `teacher_course` | (无实体) | 教师-课程 N:M 关联 |
| 中间表 | `role_menu` | (无实体) | 角色-菜单 N:M 关联 |

### 5.3 实体继承体系

```
Serializable
├── BaseEntity (空抽象类, 标记接口)
│   ├── User
│   ├── UserAuth
│   ├── UserPlatform
│   ├── Admin
│   ├── Student
│   ├── Permission
│   └── RoleBaseEntity (抽象类, +userId +isAvailable +username)
│       ├── Teacher (+teacherId +institutionId)
│       └── Parent (+parentId +phone)
├── CourseRecord
├── Record
├── PermissionRecord
├── Menu
├── ClassSchedule
├── Institution
├── Course
├── Class
├── ClassStudent
├── ParentStudent
└── ClassTeacher
```

### 5.4 关键索引与约束

| 表 | 约束 | 说明 |
|----|------|------|
| `permission_record` | `UNIQUE(user_id, course_record_id)` | 用户对同一课程记录只有一条权限记录（`ON DUPLICATE KEY UPDATE`） |
| `class_student` | `UNIQUE(class_id, student_id)` | 同一学生在同一班级只能有一条记录 |
| `parent_student` | `UNIQUE(parent_id, student_id)` | 同一家长对同一学生只有一条关联 |
| 全部表 | 主键雪花ID | 使用 `IdType.ASSIGN_ID` Snowflake 算法 |

---

## 六、API 接口设计规范

### 6.1 接口风格

全项目统一使用 **POST + @RequestBody** 方式，即使在 RESTful 语义上属于查询的操作。

**统一响应格式**：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 6.2 接口分组

#### 认证授权服务 (`/auth/**` → auth-service)

| Gateway 路径 | 服务路径 | Controller | 接口数 | 说明 |
|-------------|----------|------------|--------|------|
| `/auth/auth` | `/auth` | AuthController | 8 | 认证（OpenId/登录/注册/登出/刷新Token） |
| `/auth/menu` | `/menu` | MenuController | 1 | 菜单查询（按角色） |
| `/auth/permission_record` | `/permission_record` | PermissionRecordController | 2 | 权限记录绑定与查询 |

#### 核心业务服务 (`/biz/**` → business-service)

| Gateway 路径 | 服务路径 | Controller | 接口数 | 说明 |
|-------------|----------|------------|--------|------|
| `/biz/institution` | `/institution` | InstitutionController | 5 | 机构查询与更新 |
| `/biz/student` | `/student` | StudentController | 8 | 学生多维度查询 + 增改 |
| `/biz/teacher` | `/teacher` | TeacherController | 4 | 教师查询 + 增改 |
| `/biz/class` | `/class` | ClassController | 8 | 班级管理（增改查 + 学生进出班） |
| `/biz/course` | `/course` | CourseController | 4 | 课程查询 + 增改 |
| `/biz/course_record` | `/course_record` | CourseRecordController | 10 | 课程记录 CRUD + 扣课 + 多维度查询 |
| `/biz/record` | `/record` | RecordController | 4 | 课时记录查询 + 单/批量新增 |
| `/biz/class_schedule` | `/class_schedule` | ClassScheduleController | 4 | 班级排课查询与更新 |

### 6.3 认证接口列表

| 方法 | Gateway 路径 | 服务路径 | 公开 | 说明 |
|------|-------------|----------|------|------|
| POST | `/auth/auth/get_open_id` | `/auth/get_open_id` | ✅ | 获取微信 OpenId |
| POST | `/auth/auth/register` | `/auth/register` | ✅ | 注册 |
| POST | `/auth/auth/login_no_pwd` | `/auth/login_no_pwd` | ✅ | 微信免密登录 |
| POST | `/auth/auth/login_by_pwd` | `/auth/login_by_pwd` | ✅ | 密码登录 |
| POST | `/auth/auth/login_by_token` | `/auth/login_by_token` | ✅ | Token 登录 |
| POST | `/auth/auth/refresh` | `/auth/refresh` | ✅ | 刷新 AccessToken |
| POST | `/auth/auth/logout` | `/auth/logout` | ❌ | 登出 |
| POST | `/auth/auth/get_user_auth_info_by_teacher_id` | `/auth/get_user_auth_info_by_teacher_id` | ❌ | 按教师查询认证信息 |

### 6.4 核心业务接口

**课时记录管理** (`/biz/record`)：

| 接口 | 说明 | 核心参数 |
|------|------|----------|
| `POST /biz/record/get` | 查询课时变更记录 | courseRecordId 分页 |
| `POST /biz/record/new_get` | 新版查询 | 扩展筛选条件 |
| `POST /biz/record/add` | 单条记录新增 | 课时数量变更 |
| `POST /biz/record/add_all` | 批量记录新增 | 多条课时变更 |

**课程记录管理** (`/biz/course_record`)：

| 接口 | 说明 | 核心参数 |
|------|------|----------|
| `POST /biz/course_record/get` | 分页查询 | 多条件筛选 + 分页 |
| `POST /biz/course_record/new_get` | 新版分页查询 | 扩展筛选条件 |
| `POST /biz/course_record/add` | 新增购课记录（旧校验组） | studentId + courseId + 课时数 |
| `POST /biz/course_record/insert` | 新增购课记录（新校验组） | studentId + courseId + 课时数 |
| `POST /biz/course_record/update` | 更新购课记录 | 状态/备注/课时 |
| `POST /biz/course_record/delete` | 删除（逻辑删除） | id |
| `POST /biz/course_record/deduct_by_student_id` | 按学生扣课 | studentId + 班级列表 |
| `POST /biz/course_record/deduct_by_course_id` | 按课程扣课 | courseId + 扣课数量 |
| `POST /biz/course_record/deduct_by_class_id` | 按班级扣课 | classId + 扣课数量 |
| `POST /biz/course_record/get_by_student_id` | 按学生查询课程记录 | studentId |

**学生管理** (`/biz/student`)：

| 接口 | 说明 |
|------|------|
| `POST /biz/student/get_by_student_id` | 按学生ID查询 |
| `POST /biz/student/get_by_parent_id` | 按家长查学生 |
| `POST /biz/student/get_by_teacher_id` | 按教师查学生 |
| `POST /biz/student/get_by_class_id` | 按班级查学生 |
| `POST /biz/student/get_by_institution_id` | 按机构查学生 |
| `POST /biz/student/get_by_course_id` | 按课程查学生 |
| `POST /biz/student/insert` | 新增学生 |
| `POST /biz/student/update` | 更新学生信息 |

**班级管理** (`/biz/class`)：

| 接口 | 说明 |
|------|------|
| `POST /biz/class/get_class_by_id` | 按ID查询班级 |
| `POST /biz/class/get_classes_by_student_id` | 按学生查询班级 |
| `POST /biz/class/get_classes_by_teacher_id` | 按教师查询班级 |
| `POST /biz/class/get_classes_by_institution_id` | 按机构查询班级 |
| `POST /biz/class/add_student_to_class` | 添加学生到班级 |
| `POST /biz/class/remove_student_from_class` | 从班级移除学生 |
| `POST /biz/class/insert` | 新增班级 |
| `POST /biz/class/update_by_id` | 更新班级信息 |

---

## 七、部署架构

### 7.1 服务启动顺序

```powershell
# 1. 启动 Nacos（必须先启动）
cd nacos/bin && startup.cmd -m standalone

# 2. 启动 Sentinel Dashboard（可选）
java -jar sentinel-dashboard.jar

# 3. 启动微服务
cd gateway && mvn spring-boot:run           # :9999
cd auth-service && mvn spring-boot:run      # :10002
cd business-service && mvn spring-boot:run  # :10001
```

### 7.2 Docker 部署

| 模块 | Dockerfile | 基础镜像 | EXPOSE 端口 |
|------|-----------|----------|-------------|
| gateway | `gateway/Dockerfile` | eclipse-temurin:21-jre-alpine | 9999 |
| auth-service | `auth-service/Dockerfile` | eclipse-temurin:21-jre-alpine | 10002 |
| business-service | `business-service/Dockerfile` | eclipse-temurin:21-jre-alpine | 10001 |

---

## 八、配置说明

### 8.1 多环境配置

| 文件 | Profile | 数据库地址 | 说明 |
|------|---------|------------|------|
| `application.yml` | 默认 | `${database-address}` | 公共配置 + 占位符 |
| `application-dev.yml` | dev | `121.196.229.10` | 开发环境，日志 DEBUG + SQL 日志 |
| `application-prod.yml` | prod | `localhost` | 生产环境 |
| `application-test.yml` | test | H2 in-memory | 测试环境，排除 MyBatisPlus 自动配置 |

### 8.2 关键配置项

| 配置项 | 值 | 说明 |
|--------|-----|------|
| `server.port` | 9999/10002/10001 | 各服务端口 |
| `spring.threads.virtual.enabled` | true | 启用虚拟线程 |
| `crypto.sm2.private-key` | (私钥) | SM2 私钥，用于解密 |
| `uni-app.wx.app-id` | `wx8b6acdafc6f87e0c` | 微信小程序 AppId |
| `uni-app.wx.secret` | (密钥) | 微信小程序 Secret |
| `jwt.secret` | `class-times-record-secret-key-2024` | JWT 签名密钥 |
| `jwt.expiration` | `86400000` | Token 过期时间（24h，毫秒） |
| `mybatis-plus.global-config.db-config.logic-delete-field` | `isDeleted` | 逻辑删除字段 |
| `mybatis-plus.global-config.db-config.id-type` | `ASSIGN_ID` | 雪花算法主键 |

---

## 九、后续演进建议

1. **中间表实体化**：`teacher_course`、`role_menu` 目前无实体类，建议创建对应实体和 Mapper 接口
2. **签名 Nonce 去重**：建议引入 Redis 实现 `x-nonce` 一次性校验，提升防重放能力
3. **API 版本管理**：当前 `new_get`、`newGetCourseRecords` 等以命名区分新旧接口，建议采用 `/v1/`、`/v2/` 版本路径
4. **接口风格**：查询类接口可考虑改为 GET 请求，更符合 RESTful 语义
5. **配置中心**：当前使用本地 yml 配置，可迁移到 Nacos Config 实现动态配置管理
6. **链路追踪**：建议引入 SkyWalking 或 Sleuth + Zipkin 实现分布式链路追踪
7. **跨服务调用**：business-service 中 TeacherServiceImpl 对 UserMapper 的跨域引用，可改为通过 auth-service 的 Feign/RPC 接口调用
