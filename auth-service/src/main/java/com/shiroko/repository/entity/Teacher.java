package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shiroko.repository.entity.common.RoleBaseEntity;
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

    /**
     * 机构id
     */
    @TableField(value = "institution_id")
    private Long institutionId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}