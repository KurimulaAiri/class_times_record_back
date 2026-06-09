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
 * Description: 用户平台关联实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/27 下午14:06
 */
@TableName(value = "user_platform")
@Data
public class UserPlatform implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 平台用户唯一id
     */
    private String openId;
    /**
     * 平台跨应用唯一id
     */
    private String unionId;
    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 上次登录角色
     */
    private Long lastLoginRole;
    /**
     * 平台
     */
    private String platform;
    /**
     * 是否可用
     */
    private Boolean isAvailable;
    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;


}