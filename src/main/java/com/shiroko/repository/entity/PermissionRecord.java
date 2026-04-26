package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * Description: 管理员分组记录实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/21 下午14:35
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="permission_record")
@Data
@Accessors(chain = true)
public class PermissionRecord extends BaseEntity {
    /**
     * 权限记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程记录id
     */
    @TableField(value = "course_record_id")
    private Long courseRecordId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 权限类型 guest 访客 admin 管理员
     */
    @TableField(value = "permission_type")
    private Long permissionType;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}