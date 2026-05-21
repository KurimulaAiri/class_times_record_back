# 课时记录系统 - 测试计划

## 1. 概述

### 1.1 项目背景

本项目是一个基于 Spring Boot 4.0.4 + MyBatis-Plus 3.5.16 的课时记录管理系统，支持课程记录管理、学生管理、课时扣减/增加、JWT
鉴权等核心业务。

### 1.2 测试目标

- 验证核心业务逻辑的正确性
- 确保边界条件和异常场景得到正确处理
- 保证数据一致性（如课时扣减与记录插入的事务完整性）
- 验证 JWT Token 的生成、校验、解析功能

## 2. 测试环境

| 项目           | 版本/说明                               |
|--------------|-------------------------------------|
| JDK          | 21 (Oracle)                         |
| Maven        | 3.6.1                               |
| Spring Boot  | 4.0.4                               |
| MyBatis-Plus | 3.5.16                              |
| 测试框架         | JUnit 5 (Jupiter 6.0.3)             |
| Mock 框架      | Mockito 5.20.0 + MockitoExtension   |
| 断言库          | JUnit 5 Assertions + Mockito Verify |
| 数据库          | H2 (内存数据库，测试用)                      |
| 构建插件         | maven-surefire-plugin 3.0.0         |

## 3. 测试策略

### 3.1 测试层次

```
┌─────────────────────────────────┐
│        单元测试 (Unit Test)       │  ← 当前阶段
│   JUnit 5 + Mockito             │
│   Mock 所有外部依赖（Mapper等）    │
├─────────────────────────────────┤
│      集成测试 (Integration)       │  ← 待实施
│   @SpringBootTest + H2          │
├─────────────────────────────────┤
│    端到端测试 (End-to-End)        │  ← 待实施
│   @SpringBootTest + MockMvc     │
└─────────────────────────────────┘
```

### 3.2 当前测试范围

当前阶段聚焦于 **单元测试**，使用 Mockito 模拟所有 Mapper 和 Converter 依赖，确保 Service 层和工具类的业务逻辑正确。

### 3.3 Mock 策略

| 模拟对象                        | 原因                     |
|-----------------------------|------------------------|
| 所有 `*Mapper` 接口             | 隔离数据库操作，避免依赖真实数据库      |
| 所有 `*Converter` 接口          | 隔离 MapStruct 转换，聚焦业务逻辑 |
| `UserContext` (ThreadLocal) | 模拟当前登录用户上下文            |

## 4. 模块测试概览

| 模块     | 测试类                           | 用例数    | 状态         |
|--------|-------------------------------|--------|------------|
| JWT 鉴权 | `JwtUtilsTest`                | 12     | ✅ 全部通过     |
| 记录管理   | `RecordServiceImplTest`       | 7      | ✅ 全部通过     |
| 课程记录   | `CourseRecordServiceImplTest` | 9      | ✅ 全部通过     |
| 学生管理   | `StudentServiceImplTest`      | 9      | ✅ 全部通过     |
| **合计** |                               | **37** | **✅ 全部通过** |

## 5. 被测方法清单

### 5.1 JwtUtils

| 方法                             | 说明                  |
|--------------------------------|---------------------|
| `createToken(Long, Long)`      | 生成 JWT Token        |
| `validateToken(String)`        | 校验 Token 有效性        |
| `parseClaims(String)`          | 解析 Token 获取 Claims  |
| `getUserInfoFromToken(String)` | 解析 Token 获取用户信息 Map |

### 5.2 RecordServiceImpl

| 方法                                | 说明              |
|-----------------------------------|-----------------|
| `insertRecord(InsertRecordDTO)`   | 插入单条课时变动记录      |
| `insertRecords(InsertRecordsDTO)` | 批量插入记录并更新课程剩余课时 |
| `getRecord(QueryRecordDTO)`       | 分页查询课时变动记录      |

### 5.3 CourseRecordServiceImpl

| 方法                                          | 说明              |
|---------------------------------------------|-----------------|
| `addCourseRecord(InsertCourseRecordDTO)`    | 新增课程记录（含权限记录）   |
| `deleteCourseRecord(DeleteCourseRecordDTO)` | 逻辑删除课程记录        |
| `updateCourseRecord(UpdateCourseRecordDTO)` | 更新课程记录信息        |
| `deductByStudentId(DeductCourseRecordDTO)`  | 按学生扣课（核心业务）     |
| `getCourseRecords(QueryCourseRecordDTO)`    | 分页查询课程记录（含权限注入） |
| `newGetCourseRecords(QueryCourseRecordDTO)` | 分页查询（实体转VO方式）   |

### 5.4 StudentServiceImpl

| 方法                                       | 说明                 |
|------------------------------------------|--------------------|
| `insertStudent(InsertStudentDTO)`        | 新增学生（含家长信息）        |
| `updateStudent(UpdateStudentDTO)`        | 更新学生及家长信息          |
| `getStudentByStudentId(QueryStudentDTO)` | 按学生ID查询（含家长注入）     |
| `getStudentByParentId(QueryStudentDTO)`  | 按家长ID分页查询学生        |
| `getStudentByTeacherId(QueryStudentDTO)` | 按教师ID分页查询学生        |
| `getStudentByClassId(QueryStudentDTO)`   | 按班级ID分页查询学生        |
| `getStudent(QueryStudentDTO)`            | 通用查询（已知未实现，返回null） |

## 6. 运行测试

### 6.1 运行全部测试

```bash
mvn test
```

### 6.2 运行指定测试类

```bash
mvn test -Dtest=JwtUtilsTest
mvn test -Dtest=RecordServiceImplTest
mvn test -Dtest=CourseRecordServiceImplTest
mvn test -Dtest=StudentServiceImplTest
```

### 6.3 运行指定测试方法

```bash
mvn test -Dtest=JwtUtilsTest#validateToken_withValidToken_shouldReturnTrue
```

## 7. 已知限制与后续计划

### 7.1 已知限制

- 当前仅有 Service 层单元测试，Mock 了所有 Mapper 依赖
- 未覆盖 Controller 层（HTTP 请求/响应）
- 未覆盖 Mapper XML 自定义 SQL
- 未进行集成测试（真实数据库连接）

### 7.2 后续计划

1. 添加 Controller 层集成测试（@WebMvcTest）
2. 添加 Repository 层测试（@MybatisTest + H2）
3. 添加 API 端到端测试（@SpringBootTest + MockMvc）
4. 添加安全测试（JWT 过期、SQL 注入防护）
5. 添加性能测试（批量操作的数据库压力）
