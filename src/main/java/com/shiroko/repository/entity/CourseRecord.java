package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Description: 课程记录实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/2/7 上午1:22
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="course_record")
@Data
@Accessors(chain = true)
public class CourseRecord extends BaseEntity {
    /**
     * 表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生id
     */
    @TableField(value = "student_id")
    private Long studentId;

    /**
     * 课程名
     */
    @TableField(value = "course_name")
    private String courseName;

    /**
     * 课时总数
     */
    @TableField(value = "course_total_time")
    private Long courseTotalTime;

    /**
     * 课程剩余次数
     */
    @TableField(value = "course_rest_time")
    private Long courseRestTime;

    /**
     * 课程状态
     * 0：
     * 1：未完成
     * 2：已完成
     */
    @TableField(value = "course_status")
    private Long courseStatus;

    /**
     * 上次上课时间
     */
    @TableField(value = "course_last_time")
    private LocalDateTime courseLastTime;

    /**
     * 课程归属人
     */
    @TableField(value = "course_owner_user_id")
    private Long courseOwnerUserId;

    /**
     * 课程备注
     */
    @TableField(value = "course_remark")
    private String courseRemark;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_delete")
    private Boolean isDelete;

    /**
     * 记录创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}