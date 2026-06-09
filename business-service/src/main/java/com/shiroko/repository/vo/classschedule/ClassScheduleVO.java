package com.shiroko.repository.vo.classschedule;

import com.shiroko.repository.entity.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 班级排课VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/16 下午10:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassScheduleVO {

    /**
     * 时刻表id
     */
    private Long id;

    /**
     * 关联的班级id
     */
    private Long classId;

    /**
     * 关联的班级名称
     */
    private String className;

    /**
     * 关联的老师数组
     */
    private List<Teacher> teachers;

    /**
     * 时间段开始日期
     */
    private String startDateStr;

    /**
     * 时间段结束日期
     */
    private String endDateStr;

    /**
     * 上课时间（1-7代表星期一到星期日）
     */
    private Long dayOfWeek;

    /**
     * 开始上课时间
     */
    private String startTimeStr;

    /**
     * 结束上课时间
     */
    private String endTimeStr;

    /**
     * 备注
     */
    private String remark;

    /**
     * 记录创建时间
     */
    private String createTimeStr;

    /**
     * 记录更新时间
     */
    private String updateTimeStr;

}
