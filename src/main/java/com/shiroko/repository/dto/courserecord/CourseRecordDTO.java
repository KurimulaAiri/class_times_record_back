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
    /**
     * 表id
     */
    private Long id;

    /**
     * 学生id
     */
    private Long studentId;

    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 课时总数
     */
    private Long courseTotalTime;

    /**
     * 课程剩余次数
     */
    private Long courseRestTime;

    /**
     * 课程状态
     * 0：
     * 1：未完成
     * 2：已完成
     */
    private Long courseStatus;

    /**
     * 上次上课时间
     */
    private LocalDateTime courseLastTime;

    /**
     * 课程归属人
     */
    private Long courseOwnerUserId;

    /**
     * 课程备注
     */
    private String courseRemark;

    /**
     * 逻辑删除
     */
    private Boolean isDelete;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;

    private Course course;

}
