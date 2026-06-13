package com.shiroko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiroko.common.enums.ResultCode;
import com.shiroko.converter.AdminBusinessConverter;
import com.shiroko.exception.BusinessException;
import com.shiroko.mapper.*;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.admin.*;
import com.shiroko.repository.dto.admin.institution.*;
import com.shiroko.repository.dto.admin.student.*;
import com.shiroko.repository.dto.admin.teacher.*;
import com.shiroko.repository.dto.admin.course.*;
import com.shiroko.repository.dto.admin.clazz.*;
import com.shiroko.repository.dto.admin.classschedule.*;
import com.shiroko.repository.dto.admin.courserecord.*;
import com.shiroko.repository.dto.admin.record.*;
import com.shiroko.repository.entity.*;
import com.shiroko.repository.entity.Class;
import com.shiroko.repository.entity.Record;
import com.shiroko.repository.vo.admin.*;
import com.shiroko.repository.vo.admin.institution.*;
import com.shiroko.repository.vo.admin.student.*;
import com.shiroko.repository.vo.admin.teacher.*;
import com.shiroko.repository.vo.admin.course.*;
import com.shiroko.repository.vo.admin.clazz.*;
import com.shiroko.repository.vo.admin.classschedule.*;
import com.shiroko.repository.vo.admin.courserecord.*;
import com.shiroko.repository.vo.admin.record.*;
import com.shiroko.repository.vo.parent.ParentVO;
import com.shiroko.service.AdminBusinessService;
import com.shiroko.util.InstitutionCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional()
public class AdminBusinessServiceImpl implements AdminBusinessService {

    private final InstitutionMapper institutionMapper;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final CourseMapper courseMapper;
    private final ClazzMapper clazzMapper;
    private final ClassScheduleMapper classScheduleMapper;
    private final CourseRecordMapper courseRecordMapper;
    private final RecordMapper recordMapper;
    private final ClassStudentMapper classStudentMapper;
    private final ParentStudentMapper parentStudentMapper;
    private final ParentMapper parentMapper;
    private final AdminBusinessConverter adminBusinessConverter;

    // ==================== 查询 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<AdminInstitutionListVO> listInstitutions(QueryInstitutionDTO dto) {
        Page<Institution> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<Institution> wrapper = new LambdaQueryWrapper<>();
        if (dto.getInstitutionId() != null) wrapper.eq(Institution::getId, dto.getInstitutionId());
        if (dto.getInstitutionName() != null && !dto.getInstitutionName().isEmpty()) wrapper.like(Institution::getInstitutionName, dto.getInstitutionName());
        if (dto.getStatus() != null) wrapper.eq(Institution::getStatus, dto.getStatus());
        wrapper.orderByDesc(Institution::getCreateTime);
        Page<Institution> result = institutionMapper.selectPage(page, wrapper);
        List<AdminInstitutionVO> voList = result.getRecords().stream().map(adminBusinessConverter::toInstitutionVO).collect(Collectors.toList());
        return ResponseDTO.success(new AdminInstitutionListVO(voList, result.getTotal()));
    }

    @Override
    public ResponseDTO<AdminStudentListVO> listStudents(QueryStudentDTO dto) {
        Page<Student> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (dto.getInstitutionId() != null) wrapper.eq(Student::getInstitutionId, dto.getInstitutionId());
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) wrapper.and(w -> w.like(Student::getStudentName, dto.getKeyword()).or().like(Student::getSchool, dto.getKeyword()));
        if (dto.getSex() != null) wrapper.eq(Student::getSex, dto.getSex());
        wrapper.orderByDesc(Student::getCreateTime);
        Page<Student> result = studentMapper.selectPage(page, wrapper);
        List<AdminStudentVO> voList = result.getRecords().stream().map(adminBusinessConverter::toStudentVO).collect(Collectors.toList());
        // 注入家长信息：使用 BaseMapper 方法，避免依赖 XML 映射文件
        List<Long> studentIds = result.getRecords().stream().map(Student::getId).collect(Collectors.toList());
        if (!studentIds.isEmpty()) {
            List<ParentStudent> psList = parentStudentMapper.selectList(
                    new LambdaQueryWrapper<ParentStudent>().in(ParentStudent::getStudentId, studentIds));
            if (!psList.isEmpty()) {
                List<Long> parentIds = psList.stream().map(ParentStudent::getParentId).distinct().collect(Collectors.toList());
                List<Parent> parents = parentMapper.selectBatchIds(parentIds);
                java.util.Map<Long, Parent> parentMap = parents.stream().collect(Collectors.toMap(Parent::getParentId, p -> p, (a, b) -> a));
                java.util.Map<Long, java.util.List<ParentStudent>> psGroupMap = psList.stream().collect(Collectors.groupingBy(ParentStudent::getStudentId));
                for (AdminStudentVO vo : voList) {
                    java.util.List<ParentStudent> associated = psGroupMap.getOrDefault(vo.getId(), java.util.Collections.emptyList());
                    for (ParentStudent ps : associated) {
                        Parent parent = parentMap.get(ps.getParentId());
                        if (parent == null) continue;
                        ParentVO pvo = new ParentVO();
                        pvo.setUsername(parent.getUsername());
                        pvo.setParentId(parent.getParentId());
                        pvo.setStudentId(ps.getStudentId());
                        pvo.setRelation(ps.getRelation());
                        pvo.setPhone(parent.getPhone());
                        pvo.setIsPrimary(ps.getIsPrimary() != null && ps.getIsPrimary() ? 1 : 0);
                        if (Integer.valueOf(1).equals(pvo.getIsPrimary())) vo.setPrimaryParent(pvo);
                        else vo.setSecondaryParent(pvo);
                    }
                }
            }
        }
        return ResponseDTO.success(new AdminStudentListVO(voList, result.getTotal()));
    }

    @Override
    public ResponseDTO<AdminTeacherListVO> listTeachers(QueryTeacherDTO dto) {
        Page<Teacher> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        if (dto.getInstitutionId() != null) wrapper.eq(Teacher::getInstitutionId, dto.getInstitutionId());
        if (dto.getIsAvailable() != null) wrapper.eq(Teacher::getIsAvailable, dto.getIsAvailable());
        wrapper.orderByDesc(Teacher::getTeacherId);
        Page<Teacher> result = teacherMapper.selectPage(page, wrapper);
        List<AdminTeacherVO> voList = result.getRecords().stream().map(adminBusinessConverter::toTeacherVO).collect(Collectors.toList());
        return ResponseDTO.success(new AdminTeacherListVO(voList, result.getTotal()));
    }

    @Override
    public ResponseDTO<AdminCourseListVO> listCourses(QueryCourseDTO dto) {
        Page<Course> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (dto.getInstitutionId() != null) wrapper.eq(Course::getInstitutionId, dto.getInstitutionId());
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) wrapper.like(Course::getCourseName, dto.getKeyword());
        if (dto.getCourseType() != null) wrapper.eq(Course::getCourseType, dto.getCourseType());
        if (dto.getIsAvailable() != null) wrapper.eq(Course::getIsAvailable, dto.getIsAvailable());
        wrapper.orderByDesc(Course::getCreateTime);
        Page<Course> result = courseMapper.selectPage(page, wrapper);
        List<AdminCourseVO> voList = result.getRecords().stream().map(adminBusinessConverter::toCourseVO).collect(Collectors.toList());
        return ResponseDTO.success(new AdminCourseListVO(voList, result.getTotal()));
    }

    @Override
    public ResponseDTO<AdminClassListVO> listClasses(QueryClassDTO dto) {
        Page<Class> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<Class> wrapper = new LambdaQueryWrapper<>();
        if (dto.getCourseId() != null) wrapper.eq(Class::getCourseId, dto.getCourseId());
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) wrapper.like(Class::getClassName, dto.getKeyword());
        if (dto.getStatus() != null) wrapper.eq(Class::getStatus, dto.getStatus());
        wrapper.orderByDesc(Class::getCreateTime);
        Page<Class> result = clazzMapper.selectPage(page, wrapper);
        List<AdminClassVO> voList = result.getRecords().stream().map(adminBusinessConverter::toClassVO).collect(Collectors.toList());
        return ResponseDTO.success(new AdminClassListVO(voList, result.getTotal()));
    }

    @Override
    public ResponseDTO<AdminClassScheduleListVO> listClassSchedules(QueryClassScheduleDTO dto) {
        LambdaQueryWrapper<ClassSchedule> wrapper = new LambdaQueryWrapper<>();
        if (dto.getClassId() != null) wrapper.eq(ClassSchedule::getClassId, dto.getClassId());
        if (dto.getDayOfWeek() != null) wrapper.eq(ClassSchedule::getDayOfWeek, dto.getDayOfWeek());
        wrapper.orderByAsc(ClassSchedule::getDayOfWeek).orderByAsc(ClassSchedule::getStartTime);
        List<ClassSchedule> list = classScheduleMapper.selectList(wrapper);
        List<AdminClassScheduleVO> voList = list.stream().map(adminBusinessConverter::toClassScheduleVO).collect(Collectors.toList());
        return ResponseDTO.success(new AdminClassScheduleListVO(voList, (long) voList.size()));
    }

    @Override
    public ResponseDTO<AdminCourseRecordListVO> listCourseRecords(QueryCourseRecordDTO dto) {
        Page<CourseRecord> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<CourseRecord> wrapper = new LambdaQueryWrapper<>();
        if (dto.getStudentId() != null) wrapper.eq(CourseRecord::getStudentId, dto.getStudentId());
        if (dto.getCourseId() != null) wrapper.eq(CourseRecord::getCourseId, dto.getCourseId());
        if (dto.getCourseStatus() != null) wrapper.eq(CourseRecord::getCourseStatus, dto.getCourseStatus());
        wrapper.eq(CourseRecord::getIsDelete, false);
        wrapper.orderByDesc(CourseRecord::getCreateTime);
        Page<CourseRecord> result = courseRecordMapper.selectPage(page, wrapper);
        List<AdminCourseRecordVO> voList = result.getRecords().stream().map(adminBusinessConverter::toCourseRecordVO).collect(Collectors.toList());
        return ResponseDTO.success(new AdminCourseRecordListVO(voList, result.getTotal()));
    }

    @Override
    public ResponseDTO<AdminRecordListVO> listRecords(QueryRecordDTO dto) {
        Page<Record> page = new Page<>(dto.getCurrentPage(), dto.getPageSize());
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        if (dto.getCourseRecordId() != null) wrapper.eq(Record::getCourseRecordId, dto.getCourseRecordId());
        if (dto.getRecordType() != null) wrapper.eq(Record::getRecordType, dto.getRecordType());
        wrapper.orderByDesc(Record::getCreateTime);
        Page<Record> result = recordMapper.selectPage(page, wrapper);
        List<AdminRecordVO> voList = result.getRecords().stream().map(adminBusinessConverter::toRecordVO).collect(Collectors.toList());
        return ResponseDTO.success(new AdminRecordListVO(voList, result.getTotal()));
    }

    // ==================== 机构 ====================

    private final InstitutionCodeUtil institutionCodeUtil = new InstitutionCodeUtil();

    @Override
    public ResponseDTO<AdminInstitutionVO> insertInstitution(InsertInstitutionDTO dto) {
        Institution institution = new Institution();
        institution.setInstitutionName(dto.getInstitutionName());
        institution.setInstitutionAddress(dto.getInstitutionAddress());
        institution.setInstitutionCode(""); // 先插入占位码，避免 NOT NULL 约束
        institution.setStatus(1L);
        institutionMapper.insert(institution);
        // 根据自增 ID 自动生成机构代码
        String code = institutionCodeUtil.encodeToCode(institution.getId());
        institution.setInstitutionCode(code);
        institutionMapper.updateById(institution);
        return ResponseDTO.success(adminBusinessConverter.toInstitutionVO(institution));
    }

    @Override
    public ResponseDTO<AdminInstitutionVO> updateInstitution(AdminUpdateInstitutionDTO dto) {
        Institution institution = new Institution();
        institution.setId(dto.getId());
        if (dto.getInstitutionName() != null) institution.setInstitutionName(dto.getInstitutionName());
        if (dto.getInstitutionAddress() != null) institution.setInstitutionAddress(dto.getInstitutionAddress());
        if (dto.getInstitutionCode() != null) institution.setInstitutionCode(dto.getInstitutionCode());
        if (dto.getStatus() != null) institution.setStatus(dto.getStatus());
        institutionMapper.updateById(institution);
        Institution updated = institutionMapper.selectById(dto.getId());
        return ResponseDTO.success(adminBusinessConverter.toInstitutionVO(updated));
    }

    // ==================== 学生 ====================

    @Override
    public ResponseDTO<AdminStudentVO> insertStudent(AdminInsertStudentDTO dto) {
        Student student = new Student();
        student.setStudentName(dto.getStudentName());
        student.setInstitutionId(dto.getInstitutionId());
        student.setSex(dto.getSex());
        student.setSchool(dto.getSchool());
        student.setAddress(dto.getAddress());
        studentMapper.insert(student);
        Student inserted = studentMapper.selectById(student.getId());
        return ResponseDTO.success(adminBusinessConverter.toStudentVO(inserted));
    }

    @Override
    public ResponseDTO<AdminStudentVO> updateStudent(AdminUpdateStudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        if (dto.getStudentName() != null) student.setStudentName(dto.getStudentName());
        if (dto.getSex() != null) student.setSex(dto.getSex());
        if (dto.getSchool() != null) student.setSchool(dto.getSchool());
        if (dto.getAddress() != null) student.setAddress(dto.getAddress());
        studentMapper.updateById(student);
        Student updated = studentMapper.selectById(dto.getId());
        return ResponseDTO.success(adminBusinessConverter.toStudentVO(updated));
    }

    @Override
    public ResponseDTO<String> deleteStudent(Long id) {
        studentMapper.deleteById(id);
        return ResponseDTO.success("删除成功");
    }

    // ==================== 教师 ====================

    @Override
    public ResponseDTO<AdminTeacherVO> insertTeacher(AdminInsertTeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setUsername(dto.getUsername());
        teacher.setInstitutionId(dto.getInstitutionId());
        teacher.setIsAvailable(dto.getIsAvailable() != null && dto.getIsAvailable() == 1);
        teacherMapper.insert(teacher);
        Teacher inserted = teacherMapper.selectById(teacher.getTeacherId());
        return ResponseDTO.success(adminBusinessConverter.toTeacherVO(inserted));
    }

    @Override
    public ResponseDTO<AdminTeacherVO> updateTeacher(AdminUpdateTeacherDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(dto.getId());
        if (dto.getUsername() != null) teacher.setUsername(dto.getUsername());
        if (dto.getIsAvailable() != null) teacher.setIsAvailable(dto.getIsAvailable() == 1);
        teacherMapper.updateById(teacher);
        Teacher updated = teacherMapper.selectById(dto.getId());
        return ResponseDTO.success(adminBusinessConverter.toTeacherVO(updated));
    }

    @Override
    public ResponseDTO<String> deleteTeacher(Long teacherId) {
        teacherMapper.deleteById(teacherId);
        return ResponseDTO.success("删除成功");
    }

    // ==================== 课程 ====================

    @Override
    public ResponseDTO<AdminCourseVO> insertCourse(AdminInsertCourseDTO dto) {
        Course course = new Course();
        course.setCourseName(dto.getCourseName());
        course.setCourseType(dto.getCourseType());
        course.setInstitutionId(dto.getInstitutionId());
        course.setIsAvailable(dto.getIsAvailable() == null || dto.getIsAvailable() == 1);
        courseMapper.insert(course);
        Course inserted = courseMapper.selectById(course.getId());
        return ResponseDTO.success(adminBusinessConverter.toCourseVO(inserted));
    }

    @Override
    public ResponseDTO<AdminCourseVO> updateCourse(AdminUpdateCourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        if (dto.getCourseName() != null) course.setCourseName(dto.getCourseName());
        if (dto.getCourseType() != null) course.setCourseType(dto.getCourseType());
        if (dto.getIsAvailable() != null) course.setIsAvailable(dto.getIsAvailable() == 1);
        courseMapper.updateById(course);
        Course updated = courseMapper.selectById(dto.getId());
        return ResponseDTO.success(adminBusinessConverter.toCourseVO(updated));
    }

    @Override
    public ResponseDTO<String> deleteCourse(Long id) {
        courseMapper.deleteById(id);
        return ResponseDTO.success("删除成功");
    }

    // ==================== 班级 ====================

    @Override
    public ResponseDTO<AdminClassVO> insertClass(AdminInsertClassDTO dto) {
        Class clazz = new Class();
        clazz.setClassName(dto.getClassName());
        clazz.setCourseId(dto.getCourseId());
        clazz.setStudentMaxCount(Long.valueOf(dto.getStudentMaxCount()));
        clazz.setStatus(dto.getStatus() != null ? dto.getStatus() : 0L);
        clazzMapper.insert(clazz);
        Class inserted = clazzMapper.selectById(clazz.getId());
        return ResponseDTO.success(adminBusinessConverter.toClassVO(inserted));
    }

    @Override
    public ResponseDTO<AdminClassVO> updateClass(AdminUpdateClassDTO dto) {
        Class clazz = new Class();
        clazz.setId(dto.getClassId());
        if (dto.getClassName() != null) clazz.setClassName(dto.getClassName());
        if (dto.getStudentMaxCount() != null) clazz.setStudentMaxCount(Long.valueOf(dto.getStudentMaxCount()));
        if (dto.getStatus() != null) clazz.setStatus(dto.getStatus());
        clazzMapper.updateById(clazz);
        Class updated = clazzMapper.selectById(dto.getClassId());
        return ResponseDTO.success(adminBusinessConverter.toClassVO(updated));
    }

    @Override
    public ResponseDTO<String> deleteClass(Long classId) {
        clazzMapper.deleteById(classId);
        return ResponseDTO.success("删除成功");
    }

    @Override
    public ResponseDTO<AdminClassDetailVO> getClassById(Long classId) {
        Class clazz = clazzMapper.selectById(classId);
        if (clazz == null) throw new BusinessException(ResultCode.PARAM_ERROR, "班级不存在");
        List<ClassStudent> classStudents = classStudentMapper.selectList(
                new LambdaQueryWrapper<ClassStudent>().eq(ClassStudent::getClassId, classId));
        List<Student> students = new ArrayList<>();
        if (!classStudents.isEmpty()) {
            List<Long> studentIds = classStudents.stream().map(ClassStudent::getStudentId).collect(Collectors.toList());
            students = studentMapper.selectBatchIds(studentIds);
        }
        return ResponseDTO.success(new AdminClassDetailVO(List.of(clazz), students));
    }

    @Override
    public ResponseDTO<String> addStudentToClass(AdminClassStudentDTO dto) {
        if (dto.getStudents() == null || dto.getStudents().isEmpty())
            throw new BusinessException(ResultCode.PARAM_ERROR, "学生列表不能为空");
        for (AdminClassStudentDTO.StudentRef student : dto.getStudents()) {
            ClassStudent cs = new ClassStudent();
            cs.setClassId(dto.getClassId());
            cs.setStudentId(student.getId());
            classStudentMapper.insert(cs);
        }
        return ResponseDTO.success("添加成功");
    }

    @Override
    public ResponseDTO<String> removeStudentFromClass(AdminClassStudentDTO dto) {
        if (dto.getStudents() == null || dto.getStudents().isEmpty())
            throw new BusinessException(ResultCode.PARAM_ERROR, "学生列表不能为空");
        for (AdminClassStudentDTO.StudentRef student : dto.getStudents()) {
            classStudentMapper.delete(
                    new LambdaQueryWrapper<ClassStudent>()
                            .eq(ClassStudent::getClassId, dto.getClassId())
                            .eq(ClassStudent::getStudentId, student.getId()));
        }
        return ResponseDTO.success("移除成功");
    }

    // ==================== 排课 ====================

    @Override
    public ResponseDTO<AdminClassScheduleVO> updateClassSchedule(AdminUpdateClassScheduleDTO dto) {
        ClassSchedule schedule = new ClassSchedule();
        schedule.setId(dto.getId());
        if (dto.getDayOfWeek() != null) schedule.setDayOfWeek(Long.valueOf(dto.getDayOfWeek()));
        classScheduleMapper.updateById(schedule);
        ClassSchedule updated = classScheduleMapper.selectById(dto.getId());
        return ResponseDTO.success(adminBusinessConverter.toClassScheduleVO(updated));
    }

    // ==================== 课程记录 ====================

    @Override
    public ResponseDTO<AdminCourseRecordVO> insertCourseRecord(AdminInsertCourseRecordDTO dto) {
        CourseRecord record = new CourseRecord();
        record.setStudentId(dto.getStudentId());
        record.setCourseId(dto.getCourseId());
        record.setCourseTotalTime(dto.getCourseTotalTime());
        record.setCourseRestTime(dto.getCourseRestTime() != null ? dto.getCourseRestTime() : dto.getCourseTotalTime());
        record.setCourseStatus(dto.getCourseStatus() != null ? dto.getCourseStatus() : 1L);
        record.setCourseRemark(dto.getCourseRemark());
        record.setIsDelete(false);
        courseRecordMapper.insert(record);
        CourseRecord inserted = courseRecordMapper.selectById(record.getId());
        return ResponseDTO.success(adminBusinessConverter.toCourseRecordVO(inserted));
    }

    @Override
    public ResponseDTO<AdminCourseRecordVO> updateCourseRecord(AdminUpdateCourseRecordDTO dto) {
        if (dto.getCourseTotalTime() != null && dto.getCourseRestTime() != null
                && dto.getCourseRestTime() > dto.getCourseTotalTime())
            throw new BusinessException(ResultCode.PARAM_ERROR, "剩余课时不能大于总课时");
        CourseRecord record = new CourseRecord();
        record.setId(dto.getId());
        if (dto.getCourseRestTime() != null) record.setCourseRestTime(dto.getCourseRestTime());
        if (dto.getCourseTotalTime() != null) record.setCourseTotalTime(dto.getCourseTotalTime());
        if (dto.getCourseStatus() != null) record.setCourseStatus(dto.getCourseStatus());
        if (dto.getCourseRemark() != null) record.setCourseRemark(dto.getCourseRemark());
        courseRecordMapper.updateById(record);
        CourseRecord updated = courseRecordMapper.selectById(dto.getId());
        return ResponseDTO.success(adminBusinessConverter.toCourseRecordVO(updated));
    }

    @Override
    public ResponseDTO<String> deleteCourseRecord(Long id) {
        CourseRecord record = new CourseRecord();
        record.setId(id);
        record.setIsDelete(true);
        courseRecordMapper.updateById(record);
        return ResponseDTO.success("删除成功");
    }

    // ==================== 课时记录 ====================

    @Override
    public ResponseDTO<AdminRecordVO> insertRecord(AdminInsertRecordDTO dto) {
        Record record = new Record();
        record.setCourseRecordId(dto.getCourseRecordId());
        record.setRecordType(dto.getRecordType());
        record.setRecordChange(dto.getRecordChange());
        recordMapper.insert(record);
        Record inserted = recordMapper.selectById(record.getId());
        return ResponseDTO.success(adminBusinessConverter.toRecordVO(inserted));
    }
}
