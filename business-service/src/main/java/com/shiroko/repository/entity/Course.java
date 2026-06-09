package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: 课程实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午0:07
 */
@TableName(value = "course")
@Data
public class Course implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 课程表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 课程类型：
     * 1：按次；
     * 2：按天；
     */
    private Long courseType;
    /**
     * 课程所在的机构id
     */
    private Long institutionId;

    /**
     * 是否可用
     */
    private Boolean isAvailable;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}