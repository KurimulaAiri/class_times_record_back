package com.shiroko.repository.vo.course;

import com.shiroko.repository.vo.courserecord.CourseRecordVO;
import com.shiroko.repository.vo.institution.InstitutionVO;
import lombok.Data;

/**
 * Description: 课程VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/9 上午12:36
 */
@Data
public class CourseVO {
    private Long id;
    private String courseName;
    private Long courseType;
    private Boolean available;
    private InstitutionVO institution;
    private CourseRecordVO currentStudentCourseRecord;
}
