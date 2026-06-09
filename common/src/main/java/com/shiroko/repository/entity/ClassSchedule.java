package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Description: 班级排班日程表实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/18 上午10:29
 */
@TableName(value = "class_schedule")
@Data
public class ClassSchedule implements Serializable {
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
     * 记录创建时间
     */
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}