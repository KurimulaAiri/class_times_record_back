# 课时记录系统 - API 测试文档

## 一、测试概览

| 项目      | 说明                                                   |
|---------|------------------------------------------------------|
| 测试类型    | Controller 层 API 测试（Slice Test）                      |
| 测试方式    | `MockMvcBuilders.standaloneSetup()` —— 不启动 Spring 容器 |
| Mock 策略 | `@Mock` Service 层，`@InjectMocks` Controller          |
| 框架      | JUnit 5 + Mockito + MockMvc                          |
| 总用例数    | **29**                                               |
| 通过率     | **29/29 全部通过**                                       |

---

## 二、API 端点全景

### 2.1 RecordController —— 课时变动记录 (`/record`)

```
POST /record/add          → 插入单条记录
POST /record/add_all      → 批量插入记录并更新课时
POST /record/get          → 分页查询记录
```

### 2.2 CourseRecordController —— 课程记录 (`/course_record`)

```
POST /course_record/get              → 分页查询 (自定义VO)
POST /course_record/new_get          → 分页查询 (实体转VO)
POST /course_record/add              → 新增课程记录
POST /course_record/delete           → 逻辑删除
POST /course_record/deduct_by_student_id → 按学生扣课
POST /course_record/update           → 更新课程记录
```

### 2.3 StudentController —— 学生管理 (`/student`)

```
POST /student/insert               → 新增学生
POST /student/update               → 更新学生
POST /student/get_by_student_id    → 按学生ID查询
POST /student/get_by_parent_id     → 按家长ID分页查询
POST /student/get_by_teacher_id    → 按教师ID分页查询
POST /student/get_by_class_id      → 按班级ID分页查询
POST /student/get_by_institution_id → 按机构ID分页查询
```

### 2.4 AuthController —— 认证 (`/auth`)

```
POST /auth/login_no_pwd     → 微信免密登录
POST /auth/login_by_pwd     → 密码登录
POST /auth/login_by_token   → Token登录
POST /auth/register         → 注册
POST /auth/logout           → 退出
POST /auth/get_open_id      → 获取OpenId
```

---

## 三、详细测试用例

### 3.1 RecordController (`RecordControllerApiTest`)

| 编号         | 用例       | 方法                     | 请求体                                                        | 预期                                          |
|------------|----------|------------------------|------------------------------------------------------------|---------------------------------------------|
| API-REC-01 | 插入单条记录成功 | POST `/record/add`     | `{courseRecordId:1, recordType:1, recordChange:2}`         | 200, `code=200`                             |
| API-REC-02 | 批量插入记录成功 | POST `/record/add_all` | `{courseRecordIdList:[1,2], recordType:1, recordChange:3}` | 200, `code=200`                             |
| API-REC-03 | 分页查询记录   | POST `/record/get`     | `{courseRecordId:1, currentPage:1, pageSize:10}`           | 200, `data.total=1`, `data.records[0].id=1` |
| API-REC-04 | 查询无记录    | POST `/record/get`     | `{courseRecordId:999}`                                     | 200, `data.total=0`, `data.records` 为空数组    |

### 3.2 CourseRecordController (`CourseRecordControllerApiTest`)

| 编号        | 用例       | 方法                                         | 请求体                                    | 预期                                        |
|-----------|----------|--------------------------------------------|----------------------------------------|-------------------------------------------|
| API-CR-01 | 添加课程记录成功 | POST `/course_record/add`                  | `{stuName:"张三", courseName:"数学", ...}` | 200, `code=200`                           |
| API-CR-02 | 添加缺少必填字段 | POST `/course_record/add`                  | `{}`                                   | **400 Bad Request**                       |
| API-CR-03 | 逻辑删除成功   | POST `/course_record/delete`               | `{courseRecordId:50}`                  | 200, `code=200`                           |
| API-CR-04 | 删除缺少ID   | POST `/course_record/delete`               | `{}`                                   | **400 Bad Request**                       |
| API-CR-05 | 更新课程记录   | POST `/course_record/update`               | `{id:1, courseTotalTime:30, ...}`      | 200, `code=200`                           |
| API-CR-06 | 扣课成功     | POST `/course_record/deduct_by_student_id` | `{studentId:100, classes:[{...}]}`     | 200, `code=200`, `data.res=1`             |
| API-CR-07 | 分页查询     | POST `/course_record/get`                  | `{currentPage:1, pageSize:10}`         | 200, `data.courseRecords[0].stuName="张三"` |
| API-CR-08 | 新方式分页查询  | POST `/course_record/new_get`              | `{currentPage:1, pageSize:10}`         | 200, `data.total=1`                       |

### 3.3 StudentController (`StudentControllerApiTest`)

| 编号         | 用例           | 方法                                    | 请求体                                              | 预期                                              |
|------------|--------------|---------------------------------------|--------------------------------------------------|-------------------------------------------------|
| API-STU-01 | 创建学生成功       | POST `/student/insert`                | `{studentName:"张三", sex:1, primaryParent:{...}}` | 200, `data.studentId=100`                       |
| API-STU-02 | 更新学生成功       | POST `/student/update`                | `{id:100, studentName:"张三更新", ...}`              | 200, `code=200`                                 |
| API-STU-03 | 按学生ID查询      | POST `/student/get_by_student_id`     | `{studentId:100}`                                | 200, `data.list[0].studentName="张三"`            |
| API-STU-04 | 按学生ID查询缺少ID  | POST `/student/get_by_student_id`     | `{}`                                             | **400 Bad Request**                             |
| API-STU-05 | 按家长ID分页查询    | POST `/student/get_by_parent_id`      | `{parentId:200, currentPage:1, pageSize:10}`     | 200, `data.list[0].studentName="张三"`            |
| API-STU-06 | 按教师ID分页查询    | POST `/student/get_by_teacher_id`     | `{teacherId:300, ...}`                           | 200, `data.list[0].studentName="李四"`            |
| API-STU-07 | 按班级ID查询(含家长) | POST `/student/get_by_class_id`       | `{classId:10, ...}`                              | 200, `data.list[0].primaryParent.username="父亲"` |
| API-STU-08 | 按机构ID分页查询    | POST `/student/get_by_institution_id` | `{institutionId:1, ...}`                         | 200, `data.list[0].studentName="赵六"`            |

### 3.4 AuthController (`AuthControllerApiTest`)

| 编号          | 用例         | 方法                          | 请求体                                                      | 预期                                          |
|-------------|------------|-----------------------------|----------------------------------------------------------|---------------------------------------------|
| API-AUTH-01 | 微信免密登录成功   | POST `/auth/login_no_pwd`   | `{code:"wx_code_001", role:2}`                           | 200, `data.token` 存在                        |
| API-AUTH-02 | 免密登录缺少code | POST `/auth/login_no_pwd`   | `{}`                                                     | **400 Bad Request**                         |
| API-AUTH-03 | 密码登录成功     | POST `/auth/login_by_pwd`   | `{openId:"...", role:1, account:"admin", ...}`           | 200, `data.token` 存在                        |
| API-AUTH-04 | 密码登录缺字段    | POST `/auth/login_by_pwd`   | `{}`                                                     | **400 Bad Request**                         |
| API-AUTH-05 | Token登录成功  | POST `/auth/login_by_token` | `{openId:"...", token:"...", needValidateAdmin:false}`   | 200, `data.token="refreshed_token"`         |
| API-AUTH-06 | 注册成功       | POST `/auth/register`       | `{account:"test", password:"...", role:2, openId:"..."}` | 200, `data.openId` 正确                       |
| API-AUTH-07 | 注册缺字段      | POST `/auth/register`       | `{}`                                                     | **400 Bad Request**                         |
| API-AUTH-08 | 退出登录成功     | POST `/auth/logout`         | `{token:"token_to_logout"}`                              | 200, `code=200`                             |
| API-AUTH-09 | 获取OpenId成功 | POST `/auth/get_open_id`    | `{code:"wx_code_001"}`                                   | 200, `data.openId="wx_open_id_from_server"` |

---

## 四、测试架构说明

### 4.1 为什么使用 standaloneSetup？

Spring Boot 4.0 进行了模块化重构：

- `@WebMvcTest` 从 `org.springframework.boot.test.autoconfigure.web.servlet`
  移至 `org.springframework.boot.webmvc.test.autoconfigure`
- `@MockBean` 替换为 `@MockitoBean`（Spring Framework 7.0）
- `spring-boot-starter-test` 拆分为 `spring-boot-starter-webmvc-test` 等模块化 starter
- `@WebMvcTest` 在 SB4 中会加载完整应用上下文（包括 MyBatis Mapper），而项目中 Mapper 需要 `SqlSessionFactory`

为避免与 MyBatis/数据库自动配置冲突，本项目的 API 测试采用 **`MockMvcBuilders.standaloneSetup()`** 方式，不启动 Spring
容器，只测试 Controller → Service 的调用链路。

### 4.2 测试层次关系

```
┌────────────────────────────────────┐
│  standaloneSetup (Controller Test) │  ← 当前 API 测试
│  验证: 路由 | 参数绑定 | 校验 | 响应  │
├────────────────────────────────────┤
│  MockitoExtension (Service Test)    │  ← 已完成（37条）
│  验证: 业务逻辑 | 事务 | 异常处理      │
└────────────────────────────────────┘
```

### 4.3 已验证的能力

| 能力                               | 验证方式                                               |
|----------------------------------|----------------------------------------------------|
| URL 路由映射                         | MockMvc 按真实路径发起 POST 请求                            |
| JSON 请求体反序列化                     | ObjectMapper + Jackson JavaTimeModule              |
| `@Valid` / `@Validated` 参数校验     | 发送缺失字段的 JSON，断言 400 Bad Request                    |
| 分组校验 (`@Validated(Group.class)`) | 发送缺少分组内必填字段的 JSON                                  |
| 响应 JSON 结构                       | `jsonPath("$.code")` / `jsonPath("$.data.xxx")` 断言 |
| Service 调用验证                     | `verify(service).method(any())`                    |
| Service 未调用验证                    | `verify(service, never()).method(any())`           |

---

## 五、运行测试

```bash
# 运行全部测试（Service + API 共 66 条）
mvn test

# 仅运行 API 测试
mvn test -Dtest="*ControllerApiTest"

# 运行指定模块的 API 测试
mvn test -Dtest="RecordControllerApiTest"
mvn test -Dtest="AuthControllerApiTest"
mvn test -Dtest="CourseRecordControllerApiTest"
mvn test -Dtest="StudentControllerApiTest"

# 运行全部测试并生成报告
mvn test surefire-report:report
```

---

## 六、测试源文件路径

| 文件                            | 路径                                                                                                                                                                                           |
|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| RecordControllerApiTest       | [src/test/java/com/shiroko/controller/RecordControllerApiTest.java](file:///d:/PRJ/javaprj/class_times_record/src/test/java/com/shiroko/controller/RecordControllerApiTest.java)             |
| CourseRecordControllerApiTest | [src/test/java/com/shiroko/controller/CourseRecordControllerApiTest.java](file:///d:/PRJ/javaprj/class_times_record/src/test/java/com/shiroko/controller/CourseRecordControllerApiTest.java) |
| StudentControllerApiTest      | [src/test/java/com/shiroko/controller/StudentControllerApiTest.java](file:///d:/PRJ/javaprj/class_times_record/src/test/java/com/shiroko/controller/StudentControllerApiTest.java)           |
| AuthControllerApiTest         | [src/test/java/com/shiroko/controller/AuthControllerApiTest.java](file:///d:/PRJ/javaprj/class_times_record/src/test/java/com/shiroko/controller/AuthControllerApiTest.java)                 |
