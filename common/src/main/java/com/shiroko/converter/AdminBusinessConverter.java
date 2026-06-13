package com.shiroko.converter;

import com.shiroko.annotation.BaseDateTimeToString;
import com.shiroko.annotation.BaseDateToString;
import com.shiroko.annotation.BaseTimeToString;
import com.shiroko.repository.entity.ClassSchedule;
import com.shiroko.repository.entity.Course;
import com.shiroko.repository.entity.CourseRecord;
import com.shiroko.repository.entity.Institution;
import com.shiroko.repository.entity.Student;
import com.shiroko.repository.entity.Teacher;
import com.shiroko.repository.vo.admin.classschedule.AdminClassScheduleVO;
import com.shiroko.repository.vo.admin.clazz.AdminClassVO;
import com.shiroko.repository.vo.admin.course.AdminCourseVO;
import com.shiroko.repository.vo.admin.courserecord.AdminCourseRecordVO;
import com.shiroko.repository.vo.admin.institution.AdminInstitutionVO;
import com.shiroko.repository.vo.admin.record.AdminRecordVO;
import com.shiroko.repository.vo.admin.student.AdminStudentVO;
import com.shiroko.repository.vo.admin.teacher.AdminTeacherVO;
import com.shiroko.util.DateTransformUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DateTransformUtils.class})
public interface AdminBusinessConverter {

    // Institution
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    AdminInstitutionVO toInstitutionVO(Institution institution);
    List<AdminInstitutionVO> toInstitutionVOList(List<Institution> institutions);

    // Student
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "birthStr", source = "birth", qualifiedBy = BaseDateToString.class)
    AdminStudentVO toStudentVO(Student student);
    List<AdminStudentVO> toStudentVOList(List<Student> students);

    // Teacher
    AdminTeacherVO toTeacherVO(Teacher teacher);
    List<AdminTeacherVO> toTeacherVOList(List<Teacher> teachers);

    // Course
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    AdminCourseVO toCourseVO(Course course);
    List<AdminCourseVO> toCourseVOList(List<Course> courses);

    // Class
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    AdminClassVO toClassVO(com.shiroko.repository.entity.Class clazz);
    List<AdminClassVO> toClassVOList(List<com.shiroko.repository.entity.Class> classes);

    // ClassSchedule
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "startDateStr", source = "startDate", qualifiedBy = BaseDateToString.class)
    @Mapping(target = "endDateStr", source = "endDate", qualifiedBy = BaseDateToString.class)
    @Mapping(target = "startTimeStr", source = "startTime", qualifiedBy = BaseTimeToString.class)
    @Mapping(target = "endTimeStr", source = "endTime", qualifiedBy = BaseTimeToString.class)
    AdminClassScheduleVO toClassScheduleVO(ClassSchedule classSchedule);
    List<AdminClassScheduleVO> toClassScheduleVOList(List<ClassSchedule> classSchedules);

    // CourseRecord
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "courseLastTimeStr", source = "courseLastTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "expireTimeStr", source = "expireTime", qualifiedBy = BaseDateTimeToString.class)
    AdminCourseRecordVO toCourseRecordVO(CourseRecord courseRecord);
    List<AdminCourseRecordVO> toCourseRecordVOList(List<CourseRecord> courseRecords);

    // Record
    @Mapping(target = "createTimeStr", source = "createTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "updateTimeStr", source = "updateTime", qualifiedBy = BaseDateTimeToString.class)
    @Mapping(target = "recordTimeStr", source = "recordTime", qualifiedBy = BaseDateTimeToString.class)
    AdminRecordVO toRecordVO(com.shiroko.repository.entity.Record record);
    List<AdminRecordVO> toRecordVOList(List<com.shiroko.repository.entity.Record> records);
}
