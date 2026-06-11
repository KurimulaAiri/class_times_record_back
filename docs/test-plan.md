# 课时记录系统 - 测试计划

## 1. 概述

### 1.1 项目背景

本项目是一个基于 Spring Cloud Alibaba 微服务架构的课时记录管理系统，采用 Gateway + auth-service + business-service 的架构模式，支持课程记录管理、学生管理、课时扣减/增加、JWT 鉴权等核心业务。

### 1.2 测试目标

- 验证核心业务逻辑的正确性
- 确保边界条件和异常场景得到正确处理
- 保证数据一致性（如课时扣减与记录插入的事务完整性）
- 验证 JWT Token 的生成、校验、解析功能（AccessToken + RefreshToken）
- 验证 Controller 层路由、参数校验、响应格式

## 2. 测试环境

| 项目 | 版本/说明 |
|------|----------|
| JDK | 21 (Oracle) |
| Maven | 3.9+ |
| Spring Boot | 4.0.4 |
| MyBatis-Plus | 3.5.16 |
| 测试框架 | JUnit 5 (Jupiter) |
| Mock 框架 | Mockito + MockitoExtension |
| API 测试 | MockMvc + standaloneSetup |
| 断言库 | JUnit 5 Assertions + Mockito Verify |
| 数据库 | H2 (内存数据库，测试用) |
| 构建插件 | maven-surefire-plugin |

## 3. 测试策略

### 3.1 测试层次

```
┌─────────────────────────────────┐
│      API 测试 (API Test)         │  ← 已完成
│   MockMvcBuilders.standaloneSetup│
│   验证: 路由 | 参数绑定 | 校验       │
├─────────────────────────────────┤
│      单元测试 (Unit Test)        │  ← 已完成
│   JUnit 5 + Mockito             │
│   Mock 所有外部依赖（Mapper等）    │
├─────────────────────────────────┤
│    集成测试 (Integration)        │  ← 待实施
│   @SpringBootTest + H2          │
├─────────────────────────────────┤
│  端到端测试 (End-to-End)         │  ← 待实施
│   @SpringBootTest + MockMvc     │
└─────────────────────────────────┘
```

### 3.2 当前测试范围

当前阶段聚焦于 **单元测试** 和 **Controller API 测试**：
- 使用 Mockito 模拟所有 Mapper 和 Converter 依赖，确保 Service 层和工具类的业务逻辑正确
- 使用 `MockMvcBuilders.standaloneSetup()` 测试 Controller 层的路由映射、参数校验和响应格式

### 3.3 Mock 策略

| 模拟对象 | 原因 |
|----------|------|
| 所有 `*Mapper` 接口 | 隔离数据库操作，避免依赖真实数据库 |
| 所有 `*Converter` 接口 | 隔离 MapStruct 转换，聚焦业务逻辑 |
| `UserContext` (ThreadLocal) | 模拟当前登录用户上下文 |

### 3.4 为什么使用 standaloneSetup？

为避免与 MyBatis/数据库自动配置冲突，本项目的 API 测试采用 **`MockMvcBuilders.standaloneSetup()`** 方式，不启动 Spring 容器，只测试 Controller → Service 的调用链路。测试配置中通过 `spring.autoconfigure.exclude: MybatisPlusAutoConfiguration` 排除 MyBatis 自动配置。

## 4. 模块测试概览

### 4.1 auth-service 测试（2 个文件）

| 测试类 | 类型 | 用例数 | 说明 |
|--------|------|--------|------|
| `JwtUtilsTest` | 单元测试 | 17 | JWT Token 生成、校验、解析（含 AccessToken/RefreshToken） |
| `AuthControllerApiTest` | API 测试 | 10 | 认证控制器全部端点 |

> **auth-service 小计：2 文件，27 个 @Test 方法**

### 4.2 business-service 测试（6 个文件）

| 测试类 | 类型 | 用例数 | 说明 |
|--------|------|--------|------|
| `CourseRecordServiceImplTest` | 单元测试 | 9 | 课程记录服务：增删改、扣课、查询 |
| `CourseRecordControllerApiTest` | API 测试 | 8 | 课程记录控制器 |
| `RecordServiceImplTest` | 单元测试 | 7 | 课时记录服务：单条/批量插入、查询 |
| `RecordControllerApiTest` | API 测试 | 4 | 课时记录控制器 |
| `StudentServiceImplTest` | 单元测试 | 9 | 学生服务：插入、更新、多维度查询 |
| `StudentControllerApiTest` | API 测试 | 8 | 学生控制器 |

> **business-service 小计：6 文件，45 个 @Test 方法**

### 4.3 总计

| 模块 | 测试文件数 | @Test 方法总数 |
|------|-----------|---------------|
| auth-service | 2 | 27 |
| business-service | 6 | 45 |
| **合计** | **8** | **72** |

## 5. 测试文件路径

### auth-service

| 文件 | 路径 |
|------|------|
| `JwtUtilsTest.java` | [auth-service/src/test/java/com/shiroko/JwtUtilsTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/auth-service/src/test/java/com/shiroko/JwtUtilsTest.java) |
| `AuthControllerApiTest.java` | [auth-service/src/test/java/com/shiroko/AuthControllerApiTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/auth-service/src/test/java/com/shiroko/AuthControllerApiTest.java) |

### business-service

| 文件 | 路径 |
|------|------|
| `CourseRecordServiceImplTest.java` | [business-service/src/test/java/com/shiroko/service/impl/CourseRecordServiceImplTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/service/impl/CourseRecordServiceImplTest.java) |
| `CourseRecordControllerApiTest.java` | [business-service/src/test/java/com/shiroko/controller/CourseRecordControllerApiTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/controller/CourseRecordControllerApiTest.java) |
| `RecordServiceImplTest.java` | [business-service/src/test/java/com/shiroko/service/impl/RecordServiceImplTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/service/impl/RecordServiceImplTest.java) |
| `RecordControllerApiTest.java` | [business-service/src/test/java/com/shiroko/controller/RecordControllerApiTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/controller/RecordControllerApiTest.java) |
| `StudentServiceImplTest.java` | [business-service/src/test/java/com/shiroko/service/impl/StudentServiceImplTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/service/impl/StudentServiceImplTest.java) |
| `StudentControllerApiTest.java` | [business-service/src/test/java/com/shiroko/controller/StudentControllerApiTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/controller/StudentControllerApiTest.java) |

## 6. 被测方法清单

### 6.1 JwtUtils

| 方法 | 说明 |
|------|------|
| `createToken(Long, Long)` | 生成 JWT AccessToken |
| `createRefreshToken(Long, Long)` | 生成 JWT RefreshToken |
| `validateToken(String)` | 校验 Token 有效性 |
| `parseClaims(String)` | 解析 Token 获取 Claims |
| `getUserInfoFromToken(String)` | 解析 Token 获取用户信息 Map |

### 6.2 RecordServiceImpl

| 方法 | 说明 |
|------|------|
| `insertRecord(InsertRecordDTO)` | 插入单条课时变动记录 |
| `insertRecords(InsertRecordsDTO)` | 批量插入记录并更新课程剩余课时 |
| `getRecord(QueryRecordDTO)` | 分页查询课时变动记录 |

### 6.3 CourseRecordServiceImpl

| 方法 | 说明 |
|------|------|
| `addCourseRecord(InsertCourseRecordDTO)` | 新增课程记录（含权限记录） |
| `deleteCourseRecord(DeleteCourseRecordDTO)` | 逻辑删除课程记录 |
| `updateCourseRecord(UpdateCourseRecordDTO)` | 更新课程记录信息 |
| `deductByStudentId(DeductCourseRecordDTO)` | 按学生扣课（核心业务） |
| `getCourseRecords(QueryCourseRecordDTO)` | 分页查询课程记录（含权限注入） |
| `newGetCourseRecords(QueryCourseRecordDTO)` | 分页查询（实体转VO方式） |

### 6.4 StudentServiceImpl

| 方法 | 说明 |
|------|------|
| `insertStudent(InsertStudentDTO)` | 新增学生（含家长信息） |
| `updateStudent(UpdateStudentDTO)` | 更新学生及家长信息 |
| `getStudentByStudentId(QueryStudentDTO)` | 按学生ID查询（含家长注入） |
| `getStudentByParentId(QueryStudentDTO)` | 按家长ID分页查询学生 |
| `getStudentByTeacherId(QueryStudentDTO)` | 按教师ID分页查询学生 |
| `getStudentByClassId(QueryStudentDTO)` | 按班级ID分页查询学生 |
| `getStudent(QueryStudentDTO)` | 通用查询（已知未实现，返回null） |

## 7. 运行测试

### 7.1 运行全部测试

```bash
mvn test
```

### 7.2 运行指定模块测试

```bash
mvn test -pl auth-service
mvn test -pl business-service
```

### 7.3 运行指定测试类

```bash
mvn test -Dtest=JwtUtilsTest
mvn test -Dtest=CourseRecordServiceImplTest
mvn test -Dtest=RecordServiceImplTest
mvn test -Dtest=StudentServiceImplTest
```

### 7.4 运行指定测试方法

```bash
mvn test -Dtest=JwtUtilsTest#createToken_withValidUserIdAndRoleId_shouldReturnNonEmptyToken
```

## 8. 已知限制与后续计划

### 8.1 已知限制

- 当前仅有 Service 层单元测试和 Controller API 测试，Mock 了所有 Mapper 依赖
- 未覆盖 Mapper XML 自定义 SQL
- 未进行集成测试（真实数据库连接）
- 未覆盖 Gateway 路由层测试
- ClassController、ClassScheduleController、InstitutionController 等新增控制器暂无测试覆盖

### 8.2 后续计划

1. 添加 Controller 层集成测试（@WebMvcTest）
2. 添加 Repository 层测试（@MybatisTest + H2）
3. 添加 API 端到端测试（@SpringBootTest + MockMvc）
4. 为新增控制器（ClassSchedule、Class、Course、Teacher、Institution、Menu、PermissionRecord）补充测试
5. 添加安全测试（JWT 过期、SQL 注入防护）
6. 添加 Gateway 层路由测试
7. 添加性能测试（批量操作的数据库压力）
