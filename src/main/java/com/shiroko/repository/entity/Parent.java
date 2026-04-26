package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Description: 家长类实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/24 下午21:24
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="parent")
@Data
public class Parent extends RoleBaseEntity {
    /**
     * 家长id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long parentId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}