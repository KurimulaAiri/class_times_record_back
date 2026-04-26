package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * Description: 权限实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/17 下午22:30
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="permission")
@Data
public class Permission extends BaseEntity {
    /**
     * 权限id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    @TableField(value = "permission_name")
    private String permissionName;

    /**
     * 权限权重
     */
    @TableField(value = "permission_weight")
    private Integer permissionWeight;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}