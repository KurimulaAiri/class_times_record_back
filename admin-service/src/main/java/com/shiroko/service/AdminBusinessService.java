package com.shiroko.service;

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

public interface AdminBusinessService {
    // ===== 查询 =====
    ResponseDTO<AdminInstitutionListVO> listInstitutions(QueryInstitutionDTO dto);
    ResponseDTO<AdminStudentListVO> listStudents(QueryStudentDTO dto);
    ResponseDTO<AdminTeacherListVO> listTeachers(QueryTeacherDTO dto);
    ResponseDTO<AdminCourseListVO> listCourses(QueryCourseDTO dto);
    ResponseDTO<AdminClassListVO> listClasses(QueryClassDTO dto);
    ResponseDTO<AdminClassScheduleListVO> listClassSchedules(QueryClassScheduleDTO dto);
    ResponseDTO<AdminCourseRecordListVO> listCourseRecords(QueryCourseRecordDTO dto);
    ResponseDTO<AdminRecordListVO> listRecords(QueryRecordDTO dto);

    // ===== 机构 =====
    ResponseDTO<AdminInstitutionVO> insertInstitution(InsertInstitutionDTO dto);
    ResponseDTO<AdminInstitutionVO> updateInstitution(AdminUpdateInstitutionDTO dto);

    // ===== 学生 =====
    ResponseDTO<AdminStudentVO> insertStudent(AdminInsertStudentDTO dto);
    ResponseDTO<AdminStudentVO> updateStudent(AdminUpdateStudentDTO dto);
    ResponseDTO<String> deleteStudent(Long id);

    // ===== 教师 =====
    ResponseDTO<AdminTeacherVO> insertTeacher(AdminInsertTeacherDTO dto);
    ResponseDTO<AdminTeacherVO> updateTeacher(AdminUpdateTeacherDTO dto);
    ResponseDTO<String> deleteTeacher(Long teacherId);

    // ===== 课程 =====
    ResponseDTO<AdminCourseVO> insertCourse(AdminInsertCourseDTO dto);
    ResponseDTO<AdminCourseVO> updateCourse(AdminUpdateCourseDTO dto);
    ResponseDTO<String> deleteCourse(Long id);

    // ===== 班级 =====
    ResponseDTO<AdminClassVO> insertClass(AdminInsertClassDTO dto);
    ResponseDTO<AdminClassVO> updateClass(AdminUpdateClassDTO dto);
    ResponseDTO<String> deleteClass(Long classId);
    ResponseDTO<AdminClassDetailVO> getClassById(Long classId);
    ResponseDTO<String> addStudentToClass(AdminClassStudentDTO dto);
    ResponseDTO<String> removeStudentFromClass(AdminClassStudentDTO dto);

    // ===== 排课 =====
    ResponseDTO<AdminClassScheduleVO> updateClassSchedule(AdminUpdateClassScheduleDTO dto);

    // ===== 课程记录 =====
    ResponseDTO<AdminCourseRecordVO> insertCourseRecord(AdminInsertCourseRecordDTO dto);
    ResponseDTO<AdminCourseRecordVO> updateCourseRecord(AdminUpdateCourseRecordDTO dto);
    ResponseDTO<String> deleteCourseRecord(Long id);

    // ===== 课时记录 =====
    ResponseDTO<AdminRecordVO> insertRecord(AdminInsertRecordDTO dto);
}
