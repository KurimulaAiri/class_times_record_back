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
 * Description: 家长学生数据库操作接口
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 上午1:13
 */
@TableName(value = "parent_student")
@Data
public class ParentStudent implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 家长-学生表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 家长id
     */
    private Long parentId;
    /**
     * parent与学生的关系
     */
    private String relation;
    /**
     * 学生id
     */
    private Long studentId;
    /**
     * 是否是主要家长
     */
    private Boolean isPrimary;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}