package com.shiroko.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: TODO
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
    private Integer id;

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
    private Integer courseTotalTime;

    /**
     * 课程剩余次数
     */
    private Integer courseRestTime;

    /**
     * 上次上课时间
     */
    private String courseLastTimeStr;

    /**
     * 课程归属管理群组
     */
    private Integer courseAdminGroupId;

    /**
     * 课程归属人
     */
    private Integer courseOwnerUserId;

    /**
     * 课程备注
     */
    private String courseRemark;

    /**
     * 逻辑删除
     */
    private String isDelete;

    /**
     * 记录创建时间
     */
    private String createTimeStr;

    /**
     * 记录更新时间
     */
    private String updateTimeStr;
}
