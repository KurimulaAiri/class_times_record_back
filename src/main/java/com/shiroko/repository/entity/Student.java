package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Description: 学生数据库实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/23 上午14:32
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="student")
@Data
public class Student extends BaseEntity {
    /**
     * 学生id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生姓名
     */
    @TableField(value = "student_name")
    private String studentName;

    /**
     * 学生出生日期
     */
    @TableField(value = "birth")
    private LocalDateTime birth;

    /**
     * 学生学校
     */
    @TableField(value = "school")
    private String school;

    /**
     * 学生地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 创建记录时间
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