package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * Description: 权限实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/17 下午22:40
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="user_auth")
@Data
public class UserAuth extends BaseEntity {
    /**
     * 用户权限密码表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 用户名
     */
    @TableField(value = "account")
    private String account;

    /**
     * sm3加密后的密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * sm3盐值
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 上次登录时间
     */
    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}