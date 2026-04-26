package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Description: 教师实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/15 下午22:37
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="teacher")
@Data
public class Teacher extends RoleBaseEntity {

    /**
     * 老师id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long teacherId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}