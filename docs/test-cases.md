# 课时记录系统 - 测试用例

## 一、JwtUtils 测试用例

**测试文件**: [JwtUtilsTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/auth-service/src/test/java/com/shiroko/JwtUtilsTest.java)

| 编号 | 用例名称 | 前置条件 | 输入 | 预期结果 | 类型 |
|------|----------|----------|------|----------|------|
| TC-JWT-01 | 创建AccessToken应返回非空字符串 | 无 | userId=1, roleId=2 | Token 非空且非空字符串 | 功能 |
| TC-JWT-02 | 创建AccessToken应包含三段式JWT格式 | 无 | userId=1, roleId=2 | Token 按 `.` 分割得 3 段 | 功能 |
| TC-JWT-03 | 有效AccessToken校验应返回true | 已生成合法 Token | 合法 Token | `validateToken` 返回 `true` | 功能 |
| TC-JWT-04 | 无效Token校验应返回false | 无 | `"invalid.token.here"` | `validateToken` 返回 `false` | 边界 |
| TC-JWT-05 | 空Token校验应返回false | 无 | `""` (空字符串) | `validateToken` 返回 `false` | 边界 |
| TC-JWT-06 | Null Token校验应返回false | 无 | `null` | `validateToken` 返回 `false` | 边界 |
| TC-JWT-07 | 解析有效Token应返回正确Claims | 已生成合法 Token | userId=100, roleId=200 | Claims 含 subject，userId=100，roleId=200 | 功能 |
| TC-JWT-08 | 解析被篡改的Token应抛出异常 | 已生成合法 Token | 修改签名部分的 Token | 抛出 `Exception` | 安全 |
| TC-JWT-09 | 有效Token解析用户信息应返回正确Map | 已生成合法 Token | userId=50, roleId=99 | Map 含 userId=50, roleId=99 | 功能 |
| TC-JWT-10 | 无效Token解析用户信息应返回null | 无 | `"invalid.token"` | `getUserInfoFromToken` 返回 `null` | 边界 |
| TC-JWT-11 | 空Token解析用户信息应返回null | 无 | `""` (空字符串) | `getUserInfoFromToken` 返回 `null` | 边界 |
| TC-JWT-12 | 不同用户生成的Token应不同 | 无 | 用户1: (1,1); 用户2: (2,1) | 两个 Token 不相等 | 功能 |
| TC-JWT-13 | 创建RefreshToken应返回非空字符串 | 无 | userId=1, roleId=2 | RefreshToken 非空 | 功能 |
| TC-JWT-14 | RefreshToken与AccessToken应不同 | 无 | 相同 userId/roleId | 两个 Token 不相等 | 功能 |
| TC-JWT-15 | RefreshToken可解析出相同用户信息 | 已生成 RefreshToken | userId=100, roleId=200 | Claims 含正确的 userId, roleId | 功能 |
| TC-JWT-16 | AccessToken解析为RefreshToken应失败 | 已生成 AccessToken | 按 RefreshToken 方式解析 | 解析失败或类型不匹配 | 边界 |
| TC-JWT-17 | RefreshToken解析为AccessToken应失败 | 已生成 RefreshToken | 按 AccessToken 方式解析 | 解析失败或类型不匹配 | 边界 |

---

## 二、RecordServiceImpl 测试用例

**测试文件**: [RecordServiceImplTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/service/impl/RecordServiceImplTest.java)

**被测方法**: `insertRecord`, `insertRecords`, `getRecord`

### TC-REC-01: 插入单条记录成功

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock RecordConverter 正常转换，Mock RecordMapper.insert 返回 1 |
| **输入** | `InsertRecordDTO{ courseRecordId=1, recordType=1(消课), recordChange=2, recordRemark="测试备注" }` |
| **预期** | `ResponseDTO.code = 200`，message 含"插入成功" |
| **验证点** | `recordConverter.insertDtoToPojo(dto)` 被调用 · `recordMapper.insert(mockRecord)` 被调用 |

### TC-REC-02: 插入单条记录失败

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock RecordMapper.insert 返回 0 |
| **输入** | `InsertRecordDTO{ courseRecordId=1, recordType=1, recordChange=2 }` |
| **预期** | `ResponseDTO.code = 400`，message = "插入失败" |

### TC-REC-03: 批量插入-消课类型正确扣减剩余课时

| 项目 | 内容 |
|------|------|
| **前置条件** | 两条 CourseRecord 原始剩余课时: cr1=10, cr2=5 |
| **输入** | `InsertRecordsDTO{ courseRecordIdList=[1,2], recordType=1(消课), recordChange=3 }` |
| **预期** | `code=200`；cr1 剩余课时更新为 7；cr2 剩余课时更新为 2；`courseLastTime` 设置为当前时间 |
| **验证点** | `courseRecordMapper.selectById` 被调用2次 · `updateById` 被调用2次 · `ArgumentCaptor` 验证更新值 |

### TC-REC-04: 批量插入-增加课时类型正确增加课时

| 项目 | 内容 |
|------|------|
| **前置条件** | 原始 cr: courseRestTime=10, courseTotalTime=20 |
| **输入** | `InsertRecordsDTO{ courseRecordIdList=[1], recordType=2(增加), recordChange=5 }` |
| **预期** | `code=200`；courseRestTime 更新为 15；courseTotalTime 更新为 25 |
| **验证点** | `updateById` 被调用1次 · `ArgumentCaptor` 同时验证 restTime 和 totalTime |

### TC-REC-05: 批量插入全部记录插入失败

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock RecordMapper.insert 始终返回 0 |
| **输入** | 单条课程 ID 的批量插入请求 |
| **预期** | `ResponseDTO.code = 400` |

### TC-REC-06: 分页查询返回正确分页数据

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock 分页查询返回 2 条 Record，总数 2 |
| **输入** | `QueryRecordDTO{ courseRecordId=1, currentPage=1, pageSize=10 }` |
| **预期** | `code=200`；`total=2`；`records.size=2` |
| **验证点** | `recordConverter.pojoListToVOList` 被正确调用 |

### TC-REC-07: 分页查询无记录返回空列表

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock 分页查询返回空列表 |
| **输入** | `QueryRecordDTO{ courseRecordId=999 }` |
| **预期** | `code=200`；`total=0`；`records` 为空 |

---

## 三、CourseRecordServiceImpl 测试用例

**测试文件**: [CourseRecordServiceImplTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/service/impl/CourseRecordServiceImplTest.java)

**被测方法**: `addCourseRecord`, `deleteCourseRecord`, `updateCourseRecord`, `deductByStudentId`, `getCourseRecords`, `newGetCourseRecords`

**通用前置条件**: 每个测试前 `UserContext.setUser(user)` 设置 userId=1, roleId=2；测试后 `UserContext.remove()` 清理

### TC-CR-01: 添加课程记录成功

| 项目 | 内容 |
|------|------|
| **输入** | stuName="张三", courseName="数学", courseTotalTime=20, courseRestTime=20 |
| **预期** | `code=200`；message 含"添加成功"；`courseOwnerUserId` = 当前用户ID(1) |
| **验证点** | `courseRecordMapper.insert` 被调用 · `permissionRecordMapper.insert` 被调用（自动创建管理员权限记录） |

### TC-CR-02: 逻辑删除课程记录

| 项目 | 内容 |
|------|------|
| **输入** | `courseRecordId=50` |
| **预期** | `code=200` |
| **验证点** | `updateById` 传入 `CourseRecord.id=50, isDelete=true` |

### TC-CR-03: 更新课程记录

| 项目 | 内容 |
|------|------|
| **输入** | `id=1, courseTotalTime=30, courseRestTime=15, courseStatus=1` |
| **预期** | `code=200` |
| **验证点** | `courseRecordMapper.updateById` 被调用 |

### TC-CR-04: 扣课-单课程扣减成功

| 项目 | 内容 |
|------|------|
| **输入** | `studentId=100`；单条 DeductClassDTO{ courseId=10, deductCount=3 } |
| **预期** | `code=200`；`DeductCourseRecordVO.res=1` |
| **验证点** | `updateRestTime` 以 totalCount=3 调用 · `recordMapper.insert` 插入扣课记录 |

### TC-CR-05: 扣课-同课程多班级聚合扣减数量

| 项目 | 内容 |
|------|------|
| **输入** | 两个 DeductClassDTO 指向同一 courseId=10：A班扣2节 + B班扣3节 |
| **预期** | `code=200`；`DeductCourseRecordVO.res=1` |
| **验证点** | `updateRestTime` 以 **聚合后 totalCount=5** 调用（仅一次） |

### TC-CR-06: 扣课-余额不足抛出异常

| 项目 | 内容 |
|------|------|
| **输入** | `deductCount=100`（超额），扣课课程名为"数学" |
| **预期** | 抛出 `BusinessException`，code = `ResultCode.COURSE_BALANCE_NOT_ENOUGH`(1001)，message 含"数学" |
| **验证点** | `updateRestTime` 返回 0 时抛异常 · `recordMapper.insert` **从未被调用** |

### TC-CR-07: getCourseRecords 注入权限类型

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock 自定义分页查询返回 2 条 VO（id=1,2）；Mock 权限查询返回 permType=1 对应 id=1，permType=2 对应 id=2 |
| **输入** | `currentPage=1, pageSize=10` |
| **预期** | `code=200`；`total=2`；vo1.permissionType=1 · vo2.permissionType=2 |
| **验证点** | `injectPermissionType` 方法正确回填权限字段 |

### TC-CR-08: getCourseRecords 空结果

| 项目 | 内容 |
|------|------|
| **输入** | `currentPage=1, pageSize=10`，Mock 返回空列表 |
| **预期** | `code=200`；`total=0`；`courseRecords` 列表为空 |
| **验证点** | `permissionRecordMapper.selectList` **从未被调用**（空列表跳过权限注入） |

### TC-CR-09: newGetCourseRecords 实体转VO查询

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock `selectCourseRecords` 返回 2 条 CourseRecord 实体 |
| **输入** | `currentPage=1, pageSize=10` |
| **预期** | `code=200`；`total=2`；`courseRecords.size=2` |
| **验证点** | `courseRecordConverter.pojoListToVOList` 被调用 · 权限注入正常执行 |

---

## 四、StudentServiceImpl 测试用例

**测试文件**: [StudentServiceImplTest.java](file:///d:/PRJ/fully-function-project/course_record/backend/business-service/src/test/java/com/shiroko/service/impl/StudentServiceImplTest.java)

**被测方法**: `insertStudent`, `updateStudent`, `getStudent`, `getStudentByStudentId`, `getStudentByParentId`, `getStudentByTeacherId`, `getStudentByClassId`

### TC-STU-01: insertStudent 仅含主要家长

| 项目 | 内容 |
|------|------|
| **输入** | studentName="张三", sex=1, 仅 primaryParent (secondaryParent=null) |
| **预期** | `code=200`；`studentId=100`；message 含"仅包含主要家长" |
| **验证点** | `studentMapper.insert` 1次 · `parentMapper.insert` 1次 · `parentStudentMapper.insert` 1次 |

### TC-STU-02: insertStudent 包含主次家长

| 项目 | 内容 |
|------|------|
| **输入** | studentName="李四", sex=0, 含 primaryParent + secondaryParent |
| **预期** | `code=200`；`studentId=101`；message 含"包含次要家长" |
| **验证点** | `parentMapper.insert` 2次 · `parentStudentMapper.insert` 2次 |

### TC-STU-03: updateStudent 更新已有家长

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock `parentStudentMapper.selectOne` 返回已存在关联记录 |
| **输入** | studentId=100, 更新 primaryParent (parentId=200, isPrimary=true) |
| **预期** | `code=200` |
| **验证点** | `parentMapper.insertOrUpdate` 被调用 · `parentStudentMapper.updateById` 被调用（走更新路径） |

### TC-STU-04: getStudent 未实现返回 null

| 项目 | 内容 |
|------|------|
| **输入** | 任意 `QueryStudentDTO` |
| **预期** | 方法返回 `null` |
| **说明** | 已知方法体为 `return null`，记录为已知行为 |

### TC-STU-05: getStudentByStudentId 返回含家长信息的学生

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock 查询返回 StudentDTO(id=100)；Mock 家长查询返回 1 条 Principal Parent |
| **输入** | `studentId=100` |
| **预期** | `code=200`；`list.size=1`；`student.primaryParent` 非空 |

### TC-STU-06: getStudentByStudentId 学生不存在

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock `selectByStudentId` 返回 null |
| **输入** | `studentId=999` |
| **预期** | `code=400`；message="学生不存在" |

### TC-STU-07: getStudentByParentId 分页含机构信息

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock 分页返回 1 条 StudentDTO |
| **输入** | `parentId=200, currentPage=1, pageSize=10` |
| **预期** | `code=200`；`total=1`；`list.size=1`；`institutionMapper.selectListByStudentId` 被调用 |
| **验证点** | `studentConverter.dtoListToVOList` 转换 · `institutions` 字段注入 |

### TC-STU-08: getStudentByTeacherId 空结果

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock 分页返回空列表 |
| **输入** | `teacherId=300, sex=-1`（sex=-1 时应转为 null） |
| **预期** | `code=200`；`total=0`；`list` 为空 |
| **验证点** | sex 字段在 service 中被置为 null |

### TC-STU-09: getStudentByClassId 注入家长信息

| 项目 | 内容 |
|------|------|
| **前置条件** | Mock 返回 StudentDTO(id=1)；Mock 家长批量查询返回 primaryParent(isPrimary=1) + secondaryParent(isPrimary=0) |
| **输入** | `classId=10, currentPage=1, pageSize=10` |
| **预期** | `code=200`；`student.primaryParent` 非空 · `student.secondaryParent` 非空 |
| **验证点** | 按 isPrimary 正确分流 primary/secondary 家长 |

---

## 五、测试覆盖率汇总

| 模块 | 测试文件 | 用例数 | 覆盖场景 |
|------|----------|--------|----------|
| JwtUtils | `JwtUtilsTest` | 17 | 正常 · 异常 · 边界(null/空/篡改) · AccessToken/RefreshToken |
| RecordServiceImpl | `RecordServiceImplTest` | 7 | 单条插入 · 批量消课 · 批量增加 · 失败 · 分页(有/无数据) |
| CourseRecordServiceImpl | `CourseRecordServiceImplTest` | 9 | 增删改 · 扣课(成功/聚合/余额不足) · 查询(权限注入/VO转换/空结果) |
| StudentServiceImpl | `StudentServiceImplTest` | 9 | 插入(单/双家长) · 更新 · 4种查询维度 · 已知识别(null返回) |
| AuthControllerApi | `AuthControllerApiTest` | 10 | 免密登录 · 密码登录 · Token登录 · 退出 · 注册 · OpenId · 刷新Token · 参数校验 |
| CourseRecordControllerApi | `CourseRecordControllerApiTest` | 8 | add/delete/update/deduct/get/new_get · 参数校验 |
| RecordControllerApi | `RecordControllerApiTest` | 4 | add/add_all/get(有数据/无数据) |
| StudentControllerApi | `StudentControllerApiTest` | 8 | insert/update/5种查询维度 · 参数校验 |
| **合计** | **8 文件** | **72** | **Service 单元测试 42 条 + API 测试 30 条** |

---

## 六、测试数据说明

### 6.1 Mock 数据约定

| 字段 | 约定值 | 说明 |
|------|--------|------|
| 当前用户 ID | `userId=1, roleId=2` | CourseRecordServiceImpl / StudentServiceImpl 中模拟 |
| 学生 ID | `100/101` | StudentServiceImpl 中模拟 |
| 家长 ID | `200/201/202` | StudentServiceImpl 中模拟 |
| 课程记录 ID | `1/2/10/100` | 各 Service 中模拟 |
| 初始剩余课时 | `10/20` | RecordServiceImpl 中模拟 |

### 6.2 课时操作类型枚举

| recordType | 含义 | 对 CourseRecord 的影响 |
|------------|------|------------------------|
| 1 | 消课（减少） | `courseRestTime -= recordChange` · `courseLastTime = now` |
| 2 | 增加 | `courseRestTime += recordChange` · `courseTotalTime += recordChange` |

### 6.3 业务异常码

| ResultCode | Code | 触发场景 |
|------------|------|----------|
| `COURSE_BALANCE_NOT_ENOUGH` | 1001 | 扣课时 `updateRestTime` 返回 0（余额不足） |
| `STUDENT_ALREADY_IN_CLASS` | 1002 | （未在本次测试中覆盖） |
