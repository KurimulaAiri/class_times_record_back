package com.shiroko.controller;

import com.shiroko.annotation.OperationLog;
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
import com.shiroko.repository.vo.admin.*;
import com.shiroko.repository.vo.admin.institution.*;
import com.shiroko.repository.vo.admin.student.*;
import com.shiroko.repository.vo.admin.teacher.*;
import com.shiroko.repository.vo.admin.course.*;
import com.shiroko.repository.vo.admin.clazz.*;
import com.shiroko.repository.vo.admin.classschedule.*;
import com.shiroko.repository.vo.admin.courserecord.*;
import com.shiroko.repository.vo.admin.record.*;
import com.shiroko.service.AdminBusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class AdminBusinessController {

    private final AdminBusinessService adminBusinessService;

    // ==================== 查询 ====================

    @PostMapping("/institution/list")
    public ResponseDTO<AdminInstitutionListVO> listInstitutions(@RequestBody QueryInstitutionDTO dto) {
        return adminBusinessService.listInstitutions(dto);
    }

    @PostMapping("/student/list")
    public ResponseDTO<AdminStudentListVO> listStudents(@RequestBody QueryStudentDTO dto) {
        return adminBusinessService.listStudents(dto);
    }

    @PostMapping("/teacher/list")
    public ResponseDTO<AdminTeacherListVO> listTeachers(@RequestBody QueryTeacherDTO dto) {
        return adminBusinessService.listTeachers(dto);
    }

    @PostMapping("/course/list")
    public ResponseDTO<AdminCourseListVO> listCourses(@RequestBody QueryCourseDTO dto) {
        return adminBusinessService.listCourses(dto);
    }

    @PostMapping("/class/list")
    public ResponseDTO<AdminClassListVO> listClasses(@RequestBody QueryClassDTO dto) {
        return adminBusinessService.listClasses(dto);
    }

    @PostMapping("/class_schedule/list")
    public ResponseDTO<AdminClassScheduleListVO> listClassSchedules(@RequestBody QueryClassScheduleDTO dto) {
        return adminBusinessService.listClassSchedules(dto);
    }

    @PostMapping("/course_record/list")
    public ResponseDTO<AdminCourseRecordListVO> listCourseRecords(@RequestBody QueryCourseRecordDTO dto) {
        return adminBusinessService.listCourseRecords(dto);
    }

    @PostMapping("/record/list")
    public ResponseDTO<AdminRecordListVO> listRecords(@RequestBody QueryRecordDTO dto) {
        return adminBusinessService.listRecords(dto);
    }

    // ==================== 机构 ====================

    @PostMapping("/institution/insert")
    @OperationLog("创建机构")
    public ResponseDTO<AdminInstitutionVO> insertInstitution(@RequestBody InsertInstitutionDTO dto) {
        return adminBusinessService.insertInstitution(dto);
    }

    @PostMapping("/institution/update")
    @OperationLog("更新机构")
    public ResponseDTO<AdminInstitutionVO> updateInstitution(@RequestBody AdminUpdateInstitutionDTO dto) {
        return adminBusinessService.updateInstitution(dto);
    }

    // ==================== 学生 ====================

    @PostMapping("/student/insert")
    @OperationLog("新增学生")
    public ResponseDTO<AdminStudentVO> insertStudent(@RequestBody AdminInsertStudentDTO dto) {
        return adminBusinessService.insertStudent(dto);
    }

    @PostMapping("/student/update")
    @OperationLog("更新学生")
    public ResponseDTO<AdminStudentVO> updateStudent(@RequestBody AdminUpdateStudentDTO dto) {
        return adminBusinessService.updateStudent(dto);
    }

    @PostMapping("/student/delete")
    @OperationLog("删除学生")
    public ResponseDTO<String> deleteStudent(@RequestBody Map<String, Long> params) {
        return adminBusinessService.deleteStudent(params.get("id"));
    }

    // ==================== 教师 ====================

    @PostMapping("/teacher/insert")
    @OperationLog("新增教师")
    public ResponseDTO<AdminTeacherVO> insertTeacher(@RequestBody AdminInsertTeacherDTO dto) {
        return adminBusinessService.insertTeacher(dto);
    }

    @PostMapping("/teacher/update")
    @OperationLog("更新教师")
    public ResponseDTO<AdminTeacherVO> updateTeacher(@RequestBody AdminUpdateTeacherDTO dto) {
        return adminBusinessService.updateTeacher(dto);
    }

    @PostMapping("/teacher/delete")
    @OperationLog("删除教师")
    public ResponseDTO<String> deleteTeacher(@RequestBody Map<String, Long> params) {
        return adminBusinessService.deleteTeacher(params.get("teacherId"));
    }

    // ==================== 课程 ====================

    @PostMapping("/course/insert")
    @OperationLog("新增课程")
    public ResponseDTO<AdminCourseVO> insertCourse(@RequestBody AdminInsertCourseDTO dto) {
        return adminBusinessService.insertCourse(dto);
    }

    @PostMapping("/course/update")
    @OperationLog("更新课程")
    public ResponseDTO<AdminCourseVO> updateCourse(@RequestBody AdminUpdateCourseDTO dto) {
        return adminBusinessService.updateCourse(dto);
    }

    @PostMapping("/course/delete")
    @OperationLog("删除课程")
    public ResponseDTO<String> deleteCourse(@RequestBody Map<String, Long> params) {
        return adminBusinessService.deleteCourse(params.get("id"));
    }

    // ==================== 班级 ====================

    @PostMapping("/class/insert")
    @OperationLog("新增班级")
    public ResponseDTO<AdminClassVO> insertClass(@RequestBody AdminInsertClassDTO dto) {
        return adminBusinessService.insertClass(dto);
    }

    @PostMapping("/class/update")
    @OperationLog("更新班级")
    public ResponseDTO<AdminClassVO> updateClass(@RequestBody AdminUpdateClassDTO dto) {
        return adminBusinessService.updateClass(dto);
    }

    @PostMapping("/class/delete")
    @OperationLog("删除班级")
    public ResponseDTO<String> deleteClass(@RequestBody Map<String, Long> params) {
        return adminBusinessService.deleteClass(params.get("classId"));
    }

    @PostMapping("/class/get_by_id")
    public ResponseDTO<AdminClassDetailVO> getClassById(@RequestBody Map<String, Long> params) {
        return adminBusinessService.getClassById(params.get("classId"));
    }

    @PostMapping("/class/add_student")
    @OperationLog("班级添加学生")
    public ResponseDTO<String> addStudentToClass(@RequestBody AdminClassStudentDTO dto) {
        return adminBusinessService.addStudentToClass(dto);
    }

    @PostMapping("/class/remove_student")
    @OperationLog("班级移除学生")
    public ResponseDTO<String> removeStudentFromClass(@RequestBody AdminClassStudentDTO dto) {
        return adminBusinessService.removeStudentFromClass(dto);
    }

    // ==================== 排课 ====================

    @PostMapping("/class_schedule/update")
    @OperationLog("更新排课")
    public ResponseDTO<AdminClassScheduleVO> updateClassSchedule(@RequestBody AdminUpdateClassScheduleDTO dto) {
        return adminBusinessService.updateClassSchedule(dto);
    }

    // ==================== 课程记录 ====================

    @PostMapping("/course_record/insert")
    @OperationLog("新增课程记录")
    public ResponseDTO<AdminCourseRecordVO> insertCourseRecord(@RequestBody AdminInsertCourseRecordDTO dto) {
        return adminBusinessService.insertCourseRecord(dto);
    }

    @PostMapping("/course_record/update")
    @OperationLog("更新课程记录")
    public ResponseDTO<AdminCourseRecordVO> updateCourseRecord(@RequestBody AdminUpdateCourseRecordDTO dto) {
        return adminBusinessService.updateCourseRecord(dto);
    }

    @PostMapping("/course_record/delete")
    @OperationLog("删除课程记录")
    public ResponseDTO<String> deleteCourseRecord(@RequestBody Map<String, Long> params) {
        return adminBusinessService.deleteCourseRecord(params.get("id"));
    }

    // ==================== 课时记录 ====================

    @PostMapping("/record/insert")
    @OperationLog("新增课时记录")
    public ResponseDTO<AdminRecordVO> insertRecord(@RequestBody AdminInsertRecordDTO dto) {
        return adminBusinessService.insertRecord(dto);
    }
}
