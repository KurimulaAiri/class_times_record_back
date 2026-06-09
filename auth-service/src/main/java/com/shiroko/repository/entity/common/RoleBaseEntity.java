package com.shiroko.repository.entity.common;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: 角色基础实体类
 * <p>
 * 用于定义角色表内的通用字段，本项目架构中，各种角色表与用户表通过user_id字段关联。
 * <p>
 * 在角色表 (permission) 中，定义了许多不同的角色，每个角色都有不同的权限。数据库表部分结构如下：
 * <p>
 * | id | permission_name |
 * <p>
 * 数据库内还有 permission_name 记录的名称的表，用于存储用户在每个角色下的权限的详细信息。
 * <p>
 * 例如：permission 中有一个记录的 permission_name 为 "teacher"，则在数据库中有一个表名为 teacher 的表，用于存储教师的权限信息。
 * <p>
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/26 下午1:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class RoleBaseEntity extends BaseEntity {
    /**
     * 对于user_id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 是否可用
     */
    @TableField(value = "is_available")
    private Boolean isAvailable;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

}
