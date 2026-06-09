package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shiroko.repository.entity.common.RoleBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: 管理员实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/20 上午0:21
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "admin")
@Data
public class Admin extends RoleBaseEntity implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 管理员id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long adminId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}