package com.shiroko.repository.dto.course;

import com.shiroko.repository.dto.courserecord.CourseRecordDTO;
import com.shiroko.repository.dto.institution.InstitutionDTO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description: 课程DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/9 下午4:30
 */
@Data
public class CourseDTO {
    private Long id;
    private String courseName;
    private Long courseType;
    private Long institutionId;
    private Boolean available;
    private CourseRecordDTO currentStudentCourseRecord;
    private InstitutionDTO institution;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
