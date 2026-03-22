package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Description: 记录实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午22:50
 */
@TableName(value ="record")
@Data
@Accessors(chain = true)
public class Record implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课时记录id
     */
    @TableField(value = "course_record_id")
    private Long courseRecordId;

    /**
     * 记录时间
     */
    @TableField(value = "record_time")
    private LocalDateTime recordTime;

    /**
     * 备注
     */
    @TableField(value = "record_remark")
    private String recordRemark;

    /**
     * 记录类型 1为增加 2为减少
     */
    @TableField(value = "record_type")
    private Long recordType;

    /**
     * 课时变更情况
     */
    @TableField(value = "record_change")
    private Long recordChange;

    /**
     * 记录创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 记录更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;



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
        Record other = (Record) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCourseRecordId() == null ? other.getCourseRecordId() == null : this.getCourseRecordId().equals(other.getCourseRecordId()))
            && (this.getRecordTime() == null ? other.getRecordTime() == null : this.getRecordTime().equals(other.getRecordTime()))
            && (this.getRecordRemark() == null ? other.getRecordRemark() == null : this.getRecordRemark().equals(other.getRecordRemark()))
            && (this.getRecordType() == null ? other.getRecordType() == null : this.getRecordType().equals(other.getRecordType()))
            && (this.getRecordChange() == null ? other.getRecordChange() == null : this.getRecordChange().equals(other.getRecordChange()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCourseRecordId() == null) ? 0 : getCourseRecordId().hashCode());
        result = prime * result + ((getRecordTime() == null) ? 0 : getRecordTime().hashCode());
        result = prime * result + ((getRecordRemark() == null) ? 0 : getRecordRemark().hashCode());
        result = prime * result + ((getRecordType() == null) ? 0 : getRecordType().hashCode());
        result = prime * result + ((getRecordChange() == null) ? 0 : getRecordChange().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", courseRecordId=" + courseRecordId +
                ", record_time=" + recordTime +
                ", record_remark=" + recordRemark +
                ", record_type=" + recordType +
                ", record_change=" + recordChange +
                ", create_time=" + createTime +
                ", update_time=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}