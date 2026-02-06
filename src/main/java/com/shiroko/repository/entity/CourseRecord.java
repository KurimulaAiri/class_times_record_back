package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 课程记录
 * <p>
 * TableName: course_record
 */
@TableName(value ="course_record")
@Data
public class CourseRecord implements Serializable {
    /**
     * 表id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学生姓名
     */
    @TableField(value = "stu_name")
    private String stu_name;

    /**
     * 课程名
     */
    @TableField(value = "course_name")
    private String course_name;

    /**
     * 课时总数
     */
    @TableField(value = "course_total_time")
    private Integer course_total_time;

    /**
     * 课程剩余次数
     */
    @TableField(value = "course_rest_time")
    private Integer course_rest_time;

    /**
     * 上次上课时间
     */
    @TableField(value = "course_last_time")
    private Date course_last_time;

    /**
     * 课程归属管理群组
     */
    @TableField(value = "couse_admin_group_id")
    private Integer couse_admin_group_id;

    /**
     * 课程归属人
     */
    @TableField(value = "course_owner_user_id")
    private Integer course_owner_user_id;

    /**
     * 课程备注
     */
    @TableField(value = "course_remark")
    private String course_remark;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_delete")
    private String is_delete;

    /**
     * 记录创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 记录更新时间
     */
    @TableField(value = "update_time")
    private Date update_time;

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
        CourseRecord other = (CourseRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getStu_name() == null ? other.getStu_name() == null : this.getStu_name().equals(other.getStu_name()))
            && (this.getCourse_name() == null ? other.getCourse_name() == null : this.getCourse_name().equals(other.getCourse_name()))
            && (this.getCourse_total_time() == null ? other.getCourse_total_time() == null : this.getCourse_total_time().equals(other.getCourse_total_time()))
            && (this.getCourse_rest_time() == null ? other.getCourse_rest_time() == null : this.getCourse_rest_time().equals(other.getCourse_rest_time()))
            && (this.getCourse_last_time() == null ? other.getCourse_last_time() == null : this.getCourse_last_time().equals(other.getCourse_last_time()))
            && (this.getCouse_admin_group_id() == null ? other.getCouse_admin_group_id() == null : this.getCouse_admin_group_id().equals(other.getCouse_admin_group_id()))
            && (this.getCourse_owner_user_id() == null ? other.getCourse_owner_user_id() == null : this.getCourse_owner_user_id().equals(other.getCourse_owner_user_id()))
            && (this.getCourse_remark() == null ? other.getCourse_remark() == null : this.getCourse_remark().equals(other.getCourse_remark()))
            && (this.getIs_delete() == null ? other.getIs_delete() == null : this.getIs_delete().equals(other.getIs_delete()))
            && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()))
            && (this.getUpdate_time() == null ? other.getUpdate_time() == null : this.getUpdate_time().equals(other.getUpdate_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStu_name() == null) ? 0 : getStu_name().hashCode());
        result = prime * result + ((getCourse_name() == null) ? 0 : getCourse_name().hashCode());
        result = prime * result + ((getCourse_total_time() == null) ? 0 : getCourse_total_time().hashCode());
        result = prime * result + ((getCourse_rest_time() == null) ? 0 : getCourse_rest_time().hashCode());
        result = prime * result + ((getCourse_last_time() == null) ? 0 : getCourse_last_time().hashCode());
        result = prime * result + ((getCouse_admin_group_id() == null) ? 0 : getCouse_admin_group_id().hashCode());
        result = prime * result + ((getCourse_owner_user_id() == null) ? 0 : getCourse_owner_user_id().hashCode());
        result = prime * result + ((getCourse_remark() == null) ? 0 : getCourse_remark().hashCode());
        result = prime * result + ((getIs_delete() == null) ? 0 : getIs_delete().hashCode());
        result = prime * result + ((getCreate_time() == null) ? 0 : getCreate_time().hashCode());
        result = prime * result + ((getUpdate_time() == null) ? 0 : getUpdate_time().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", stu_name=" + stu_name +
                ", course_name=" + course_name +
                ", course_total_time=" + course_total_time +
                ", course_rest_time=" + course_rest_time +
                ", course_last_time=" + course_last_time +
                ", couse_admin_group_id=" + couse_admin_group_id +
                ", course_owner_user_id=" + course_owner_user_id +
                ", course_remark=" + course_remark +
                ", is_delete=" + is_delete +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}