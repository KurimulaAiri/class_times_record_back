package com.shiroko.repository.vo;

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
    /**
     * 表id
     */
    private Long id;

    /**
     * 学生姓名
     */
    private String stuName;

    /**
     * 课程名
     */
    private String courseName;

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
     * 0：未完成
     * 1：已完成
     */
    private Long courseStatus;

    /**
     * 上次上课时间
     */
    private String courseLastTimeStr;

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
    private String isDelete;

    /**
     * 权限名称
     */
    private Long permissionType;

    /**
     * 记录创建时间
     */
    private String createTimeStr;

    /**
     * 记录更新时间
     */
    private String updateTimeStr;
}
