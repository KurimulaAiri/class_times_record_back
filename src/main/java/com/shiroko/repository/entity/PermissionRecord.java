package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Description: 管理员分组记录实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/21 下午14:35
 */
@TableName(value ="permission_record")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRecord implements Serializable {
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
    private String permissionType;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PermissionRecord other = (PermissionRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCourseRecordId() == null ? other.getCourseRecordId() == null : this.getCourseRecordId().equals(other.getCourseRecordId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPermissionType() == null ? other.getPermissionType() == null : this.getPermissionType().equals(other.getPermissionType()));
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCourseRecordId() == null) ? 0 : getCourseRecordId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPermissionType() == null) ? 0 : getPermissionType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", courseRecordId=" + courseRecordId +
                ", userId=" + userId +
                ", permissionType=" + permissionType +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}