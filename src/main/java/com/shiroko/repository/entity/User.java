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
 * Description: 用户实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 上午23:34
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="user")
@Data
public class User extends BaseEntity {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户电话号码
     */
    @TableField(value = "user_phone_num")
    private Long userPhoneNum;

    /**
     * 微信当前小程序唯一标识
     */
    @TableField(value = "open_id")
    private String openid;

    /**
     * 微信开放平台唯一标识
     */
    @TableField(value = "union_id")
    private String unionId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}