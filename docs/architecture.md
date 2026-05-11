# class_times_record 架构设计文档

## 一、系统架构概览

### 1.1 项目定位

class_times_record 是一个面向 uni-app 微信小程序的后端服务系统，提供课时记录、班级管理、学生教师管理等核心业务能力。

### 1.2 技术架构层级

```
┌─────────────────────────────────────────────────────────┐
│                   微信小程序 (uni-app)                     │
└───────────────────────────┬─────────────────────────────┘
                            │ HTTPS + JWT + SM3 签名
┌───────────────────────────▼─────────────────────────────┐
│                  Spring Boot 4.0.4                        │
│  ┌──────────────────────────────────────────────────┐   │
│  │              Filter Layer                          │   │
│  │  RequestCachingFilter (Body 缓存，支持重复读取)      │   │
│  └──────────────────────┬───────────────────────────┘   │
│  ┌──────────────────────▼───────────────────────────┐   │
│  │            Interceptor Layer                       │   │
│  │  UserInterceptor → JwtInterceptor → SignInterceptor │   │
│  └──────────────────────┬───────────────────────────┘   │
│  ┌──────────────────────▼───────────────────────────┐   │
│  │            Controller Layer (11个)                 │   │
│  │  @RestController + @RequestBody DTO               │   │
│  └──────────────────────┬───────────────────────────┘   │
│  ┌──────────────────────▼───────────────────────────┐   │
│  │            Service Layer (15对接口/实现)            │   │
│  │  + AOP (StudentCountAspect)                       │   │
│  └──────────────────────┬───────────────────────────┘   │
│  ┌──────────────────────▼───────────────────────────┐   │
│  │            Mapper Layer (MyBatis-Plus)             │   │
│  │  15个 Mapper 接口 + 15个 XML 映射文件               │   │
│  └──────────────────────┬───────────────────────────┘   │
│  ┌──────────────────────▼───────────────────────────┐   │
│  │              MySQL 8.0                             │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

### 1.3 技术栈明细

| 层级     | 技术                  | 版本          | 用途                        |
|--------|---------------------|-------------|---------------------------|
| 基础框架   | Spring Boot         | 4.0.4       | 应用框架                      |
| JDK    | Java                | 21          | 运行环境（启用虚拟线程）              |
| ORM    | MyBatis-Plus        | 3.5.16      | 数据持久层                     |
| 数据库    | MySQL               | 8.0+        | 关系型数据库                    |
| 认证     | jjwt                | 0.11.5      | JWT 令牌签发与校验               |
| API 文档 | SpringDoc OpenAPI   | 3.0.3       | Swagger UI + OpenAPI 规范   |
| 对象映射   | MapStruct           | 1.5.5.Final | DTO ↔ Entity ↔ VO 转换      |
| JSON   | Fastjson2           | 2.0.60      | JSON 序列化/反序列化             |
| 加密     | BouncyCastle        | 1.84        | SM2/SM3 国密算法              |
| 工具     | Hutool              | 5.8.40      | 通用工具类                     |
| 参数校验   | Hibernate Validator | -           | Bean Validation (JSR-303) |
| AOP    | Spring AOP          | -           | 班级人数自动统计切面                |
| 构建     | Maven               | 3.9+        | 项目构建与依赖管理                 |
| CI/CD  | Jenkins             | -           | 自动化构建与部署                  |

---

## 二、安全设计

### 2.1 三级防护体系

```
请求进入
  │
  ▼
┌──────────────────┐
│ RequestCaching    │  缓存请求 Body 到 RepeatedlyRequestWrapper
│ Filter            │  使后续拦截器可重复读取请求体
└────────┬─────────┘
         ▼
┌──────────────────┐
│ UserInterceptor   │  从 JWT 提取用户信息 → UserContext (ThreadLocal)
│ (拦截 /**)        │  提取: userId, roleType, permissionWeight ...
└────────┬─────────┘
         ▼
┌──────────────────┐
│ JwtInterceptor    │  校验 JWT 令牌有效性与过期状态
│ (排除: /auth/**   │  过期时间: 5 分钟
│  /swagger-ui/**   │  放行路径:
│  /v3/api-docs/**  │    /auth/**, /swagger-ui/**,
│  /test/**         │    /v3/api-docs/**, /test/**, /error
│  /error)          │
└────────┬─────────┘
         ▼
┌──────────────────┐
│ SignInterceptor   │  SM3 请求签名防篡改 + 防重放
│ (排除同上)        │  - 校验 x-timestamp (60秒有效期)
│                   │  - 参数字典序排序后 SM3 哈希对比
└────────┬─────────┘
         ▼
      Controller
```

### 2.2 签名机制详解

**请求头携带参数**：

| 请求头           | 说明                  |
|---------------|---------------------|
| `x-sign`      | SM3 签名哈希值           |
| `x-timestamp` | Unix 时间戳（秒），有效期 60s |
| `x-nonce`     | 随机数                 |

**签名算法流程**：

1. 收集 Query 参数与 Body JSON 参数（递归展平）
2. 将所有参数按 Key 字典序升序排列
3. 拼接为 `a=1&b=2&...&z=n` 格式
4. 追加系统盐值（配置在 `application.yml`）
5. 使用 SM3 算法计算哈希
6. 与客户端 `x-sign` 比较

### 2.3 数据加密

| 算法  | 类型    | 用途            |
|-----|-------|---------------|
| SM2 | 非对称加密 | 敏感数据传输加密（密码等） |
| SM3 | 哈希算法  | 密码摘要、请求签名校验   |

**密钥管理**：SM2 私钥配置在 `crypto.sm2.private-key` 中，不同环境可配置不同密钥。

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

| 异常类型                                                | 处理方式                           |
|-----------------------------------------------------|--------------------------------|
| `BindException` / `MethodArgumentNotValidException` | 参数校验失败，返回具体字段错误信息              |
| `CustomException`                                   | 自定义业务异常，返回异常定义的 code 与 message |
| `RuntimeException`                                  | 运行时异常，兜底处理                     |
| `SQLException`                                      | 数据库异常，返回通用错误                   |
| `Exception`                                         | 未知异常，兜底处理                      |

---

## 四、项目分层规范

### 4.1 目录与包职责

| 包路径                  | 职责        | 说明                              |
|----------------------|-----------|---------------------------------|
| `controller/`        | 接入层       | 接收请求、参数校验、调用 Service、组装响应       |
| `service/`           | 业务层接口     | 定义业务逻辑契约                        |
| `service/impl/`      | 业务层实现     | 实现业务逻辑，调用 Mapper                |
| `mapper/`            | 持久层接口     | MyBatis-Plus BaseMapper 扩展      |
| `repository/entity/` | 数据库实体     | 与表结构一一对应                        |
| `repository/dto/`    | 请求对象      | 按业务模块分包，含校验注解                   |
| `repository/vo/`     | 响应对象      | 按业务模块分包，可嵌套其他 VO                |
| `converter/`         | 对象转换      | MapStruct 自动生成转换代码              |
| `config/`            | Spring 配置 | MyBatis-Plus / Web / OpenAPI 配置 |
| `interceptor/`       | 拦截器       | 认证、签名、上下文注入                     |
| `filter/`            | 过滤器       | 请求体缓存                           |
| `aspect/`            | AOP 切面    | 班级人数自动统计                        |
| `exception/`         | 异常处理      | 全局异常处理器 + 自定义异常                 |
| `util/`              | 工具类       | JWT / SM2 / SM3                 |
| `annotation/`        | 自定义注解     | `@UpdateStudentCount`           |
| `context/`           | 上下文       | `UserContext` (ThreadLocal)     |
| `support/`           | 辅助类       | 请求包装器                           |

### 4.2 数据流转规范

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
- **跨层转换**：统一使用 MapStruct（位于 `converter/` 包）
- **HTTP 方法**：所有业务接口统一使用 POST（即使在语义上是查询操作）

### 4.3 参数校验

- 使用 Hibernate Validator 注解（`@NotNull`、`@NotBlank`、`@Valid` 等）
- 支持分组校验（`@Validated(Group.class)`）
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

| 分类   | 表名                  | 实体类                | 说明                         |
|------|---------------------|--------------------|----------------------------|
| 基础   | `user`              | `User`             | 用户基础信息(手机号/openid/unionId) |
| 认证   | `user_auth`         | `UserAuth`         | 登录凭据(账号/密码/盐值/角色)          |
| 权限   | `permission`        | `Permission`       | 角色定义(名称/权重)                |
| 菜单   | `menu`              | `Menu`             | 前端菜单配置(图标/路径/排序)           |
| 机构   | `institution`       | `Institution`      | 培训机构信息                     |
| 教师   | `teacher`           | `Teacher`          | 教师信息                       |
| 家长   | `parent`            | `Parent`           | 家长信息                       |
| 学生   | `student`           | `Student`          | 学生信息(姓名/性别/学校等)            |
| 课程   | `course`            | `Course`           | 课程信息(按次/按天)                |
| 班级   | `class`             | `Class`            | 班级信息(人数/上限)                |
| 课程记录 | `course_record`     | `CourseRecord`     | 学生购课记录(总量/剩余/状态)           |
| 课时记录 | `record`            | `Record`           | 课时变更明细(增/减)                |
| 权限记录 | `permission_record` | `PermissionRecord` | 课程记录-用户权限关联                |
| 中间表  | `class_student`     | `ClassStudent`     | 班级-学生 N:M 关联               |
| 中间表  | `parent_student`    | `ParentStudent`    | 家长-学生 N:M 关联               |
| 中间表  | `teacher_course`    | (无实体)              | 教师-课程 N:M 关联               |
| 中间表  | `role_menu`         | (无实体)              | 角色-菜单 N:M 关联               |

### 5.3 实体继承体系

```
Serializable
├── BaseEntity (空抽象类, 标记接口)
│   ├── User
│   ├── UserAuth
│   ├── Student
│   ├── Permission
│   └── RoleBaseEntity (抽象类, +userId +isAvailable +username)
│       ├── Teacher (+teacherId +institutionId)
│       └── Parent (+parentId +phone)
├── CourseRecord
├── Record
├── PermissionRecord
├── Menu
├── Institution (不继承 BaseEntity)
├── Course (不继承 BaseEntity)
├── Class (不继承 BaseEntity)
├── ClassStudent (不继承 BaseEntity)
└── ParentStudent (不继承 BaseEntity)
```

### 5.4 关键索引与约束

| 表                   | 约束                                  | 说明                                           |
|---------------------|-------------------------------------|----------------------------------------------|
| `permission_record` | `UNIQUE(user_id, course_record_id)` | 用户对同一课程记录只有一条权限记录（`ON DUPLICATE KEY UPDATE`） |
| `class_student`     | `UNIQUE(class_id, student_id)`      | 同一学生在同一班级只能有一条记录                             |
| `parent_student`    | `UNIQUE(parent_id, student_id)`     | 同一家长对同一学生只有一条关联                              |
| 全部表                 | 主键自增/雪花                             | 使用 `IdType.ASSIGN_ID` Snowflake 算法           |

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

| 路由前缀                 | Controller                 | 接口数 | 说明                    |
|----------------------|----------------------------|-----|-----------------------|
| `/auth`              | AuthController             | 6   | 认证（获取OpenId/登录/注册/登出） |
| `/class`             | ClassController            | 2   | 班级查询（按学生/教师）          |
| `/course`            | CourseController           | 1   | 课程查询（按机构）             |
| `/course_record`     | CourseRecordController     | 5   | 课程记录 CRUD + 查询        |
| `/institution`       | InstitutionController      | 1   | 机构查询（按学生）             |
| `/menu`              | MenuController             | 1   | 菜单查询（按角色）             |
| `/permission_record` | PermissionRecordController | 2   | 权限记录绑定与查询             |
| `/record`            | RecordController           | 3   | 课时记录查询 + 单/批量新增       |
| `/student`           | StudentController          | 6   | 学生多维度查询 + 增改          |
| `/teacher`           | TeacherController          | 2   | 教师查询                  |
| `/test`              | TestController             | 2   | 测试接口（GET，不受安全拦截）      |

### 6.3 认证接口列表

| 方法   | 路径                     | 说明          | 校验方式                    |
|------|------------------------|-------------|-------------------------|
| POST | `/auth/get_open_id`    | 获取微信 OpenId | 微信 code                 |
| POST | `/auth/login_no_pwd`   | 免密登录        | OpenId 校验               |
| POST | `/auth/login_by_pwd`   | 密码登录        | 账号 + SM3(password+salt) |
| POST | `/auth/login_by_token` | Token 登录    | 已有 JWT 换新               |
| POST | `/auth/logout`         | 登出          | 清除 JWT                  |
| POST | `/auth/register`       | 注册          | 账号 + 密码 + 角色            |

### 6.4 核心业务接口

**课时记录管理** (`/record`)：

| 接口                     | 说明       | 核心参数              |
|------------------------|----------|-------------------|
| `POST /record/get`     | 查询课时变更记录 | courseRecordId 分页 |
| `POST /record/add`     | 单条记录新增   | 课时数量变更            |
| `POST /record/add_all` | 批量记录新增   | 多条课时变更            |

**课程记录管理** (`/course_record`)：

| 接口                            | 说明       | 核心参数                       |
|-------------------------------|----------|----------------------------|
| `POST /course_record/get`     | 分页查询     | 多条件筛选 + 分页                 |
| `POST /course_record/new_get` | 新版分页查询   | 扩展筛选条件                     |
| `POST /course_record/add`     | 新增购课记录   | studentId + courseId + 课时数 |
| `POST /course_record/update`  | 更新购课记录   | 状态/备注/课时                   |
| `POST /course_record/delete`  | 删除（逻辑删除） | id                         |

**学生管理** (`/student`)：

| 接口                                    | 说明     |
|---------------------------------------|--------|
| `POST /student/get_by_parent_id`      | 按家长查学生 |
| `POST /student/get_by_teacher_id`     | 按教师查学生 |
| `POST /student/get_by_class_id`       | 按班级查学生 |
| `POST /student/get_by_institution_id` | 按机构查学生 |
| `POST /student/insert`                | 新增学生   |
| `POST /student/update`                | 更新学生信息 |

---

## 七、CI/CD 部署

### 7.1 Jenkins 流水线

配置文件：[pipeline/Jenkinsfile](../pipeline/Jenkinsfile)

```
Stage 1: 拉取代码
  └─ git clone (GitHub master 分支, SSH)

Stage 2: 编译打包
  └─ mvn clean package -DskipTests (JDK 21, Maven 3.9.13)

Stage 3: 发布 jar
  └─ cp target/*.jar → /opt/deploy/class_times_record_back

Stage 4: 停止旧进程
  └─ 检查 PID 文件 / 端口 9999 占用 → kill

Stage 5: 启动新进程
  └─ nohup java -jar --spring.profiles.active=prod
  └─ 等待日志中出现 Started（最长 60s）
```

### 7.2 部署目标

| 项目     | 配置                                     |
|--------|----------------------------------------|
| 宿主机    | `172.21.0.1:25184`                     |
| 应用端口   | `9999`                                 |
| JDK 路径 | `/www/server/java/jdk-21.0.2/bin/java` |
| 部署目录   | `/opt/deploy/class_times_record_back`  |

---

## 八、配置说明

### 8.1 多环境配置

| 文件                     | Profile  | 数据库地址                 | 说明            |
|------------------------|----------|-----------------------|---------------|
| `application.yml`      | 默认 (dev) | `${database-address}` | 公共配置 + 占位符    |
| `application-dev.yml`  | dev      | `121.196.229.10`      | 开发环境，日志 DEBUG |
| `application-prod.yml` | prod     | `localhost`           | 生产环境          |

### 8.2 关键配置项

| 配置项                                                       | 值                    | 说明          |
|-----------------------------------------------------------|----------------------|-------------|
| `server.port`                                             | 9999                 | 服务端口        |
| `spring.threads.virtual.enabled`                          | true                 | 启用虚拟线程      |
| `crypto.sm2.private-key`                                  | (私钥)                 | SM2 私钥，用于解密 |
| `uni-app.wx.app-id`                                       | `wx8b6acdafc6f87e0c` | 微信小程序 AppId |
| `mybatis-plus.global-config.db-config.logic-delete-field` | `isDeleted`          | 逻辑删除字段      |
| `mybatis-plus.global-config.db-config.id-type`            | `ASSIGN_ID`          | 雪花算法主键      |

---

## 九、后续演进建议

1. **中间表实体化**：`teacher_course`、`role_menu` 目前无实体类，建议创建对应实体和 Mapper 接口
2. **逻辑删除统一**：部分表（如 `course_record`）自行管理 `is_delete` 字段，可统一切换到 MyBatis-Plus 全局逻辑删除
3. **接口风格**：查询类接口可考虑改为 GET 请求，更符合 RESTful 语义
4. **签名 Nonce 去重**：建议引入 Redis 实现 `x-nonce` 一次性校验，提升防重放能力
5. **API 版本管理**：当前 `new_get` 以路径区分新旧接口，建议采用 `/v1/`、`/v2/` 版本路径
