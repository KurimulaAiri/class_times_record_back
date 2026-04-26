package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Description: 角色基础实体类
 *
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
