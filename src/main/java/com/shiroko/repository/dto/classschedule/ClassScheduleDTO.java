package com.shiroko.repository.dto.classschedule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.shiroko.repository.entity.Teacher;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Description: 班级排课DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/22 下午10:01
 */
@Data
public class ClassScheduleDTO {
    /**
     * 时刻表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的班级id
     */
    private Long classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 时间段开始日期
     */
    private LocalDate startDate;

    /**
     * 时间段结束日期
     */
    private LocalDate endDate;

    /**
     * 上课时间（1-7代表星期一到星期日）
     */
    private Long dayOfWeek;

    /**
     * 开始上课时间
     */
    private LocalTime startTime;

    /**
     * 结束上课时间
     */
    private LocalTime endTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 教师列表
     */
    private List<Teacher> teachers;

    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;

}
