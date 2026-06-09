package com.shiroko.repository.vo.courserecord;

import com.shiroko.repository.vo.course.CourseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 课程记录VO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 下午9:52
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseRecordVO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private Long courseTotalTime;
    private Long courseRestTime;
    private Long courseStatus;
    private String courseLastTimeStr;
    private String expireTimeStr;
    private Long courseOwnerUserId;
    private String courseRemark;
    private String isDelete;
    private Long permissionType;
    private String createTimeStr;
    private String updateTimeStr;
    private CourseVO course;
}
