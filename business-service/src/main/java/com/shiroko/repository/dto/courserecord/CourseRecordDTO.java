package com.shiroko.repository.dto.courserecord;

import com.shiroko.repository.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description: 课程记录DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/2 下午2:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRecordDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private Long courseTotalTime;
    private Long courseRestTime;
    private Long courseStatus;
    private LocalDateTime courseLastTime;
    private LocalDateTime expireTime;
    private Long courseOwnerUserId;
    private String courseRemark;
    private Boolean isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Course course;

}
