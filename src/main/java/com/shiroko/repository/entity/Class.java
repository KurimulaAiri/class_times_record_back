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
 * Description: 班级实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午20:27
 */
@TableName(value ="class")
@Data
public class Class implements Serializable {
    /**
     * 班级表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 班级对应的课程id
     */
    private Long courseId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 班级状态
     */
    private Long status;


    /**
     * 班级学生人数
     */
    private Long studentCount;

    /**
     * 班级最大人数
     */
    private Long studentMaxCount;

    /**
     * 创建时间
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