package com.shiroko.repository.vo.record;

import com.shiroko.repository.vo.BaseVO;
import com.shiroko.repository.vo.course.CourseVO;
import com.shiroko.repository.vo.courserecord.CourseRecordVO;
import com.shiroko.repository.vo.student.StudentVO;
import com.shiroko.repository.vo.teacher.TeacherVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Description: 记录VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午11:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordVO extends BaseVO {
    private Long id;
    private Long courseRecordId;
    private CourseRecordVO courseRecord;
    private CourseVO course;
    private StudentVO student;
    private TeacherVO operatorTeacher;
    private String recordTimeStr;
    private String recordRemark;
    private Long recordType;
    private Long recordChange;
    private String createTimeStr;
    private String updateTimeStr;
}
