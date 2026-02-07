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
    private String stuName;

    /**
     * 课程名
     */
    @TableField(value = "course_name")
    private String courseName;

    /**
     * 课时总数
     */
    @TableField(value = "course_total_time")
    private Integer courseTotalTime;

    /**
     * 课程剩余次数
     */
    @TableField(value = "course_rest_time")
    private Integer courseRestTime;

    /**
     * 上次上课时间
     */
    @TableField(value = "course_last_time")
    private Date courseLastTime;

    /**
     * 课程归属管理群组
     */
    @TableField(value = "course_admin_group_id")
    private Integer courseAdminGroupId;

    /**
     * 课程归属人
     */
    @TableField(value = "course_owner_user_id")
    private Integer courseOwnerUserId;

    /**
     * 课程备注
     */
    @TableField(value = "course_remark")
    private String courseRemark;

    /**
     * 逻辑删除
     */
    @TableField(value = "is_delete")
    private String isDelete;

    /**
     * 记录创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 记录更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

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
            && (this.getStuName() == null ? other.getStuName() == null : this.getStuName().equals(other.getStuName()))
            && (this.getCourseName() == null ? other.getCourseName() == null : this.getCourseName().equals(other.getCourseName()))
            && (this.getCourseTotalTime() == null ? other.getCourseTotalTime() == null : this.getCourseTotalTime().equals(other.getCourseTotalTime()))
            && (this.getCourseRestTime() == null ? other.getCourseRestTime() == null : this.getCourseRestTime().equals(other.getCourseRestTime()))
            && (this.getCourseLastTime() == null ? other.getCourseLastTime() == null : this.getCourseLastTime().equals(other.getCourseLastTime()))
            && (this.getCourseAdminGroupId() == null ? other.getCourseAdminGroupId() == null : this.getCourseAdminGroupId().equals(other.getCourseAdminGroupId()))
            && (this.getCourseOwnerUserId() == null ? other.getCourseOwnerUserId() == null : this.getCourseOwnerUserId().equals(other.getCourseOwnerUserId()))
            && (this.getCourseRemark() == null ? other.getCourseRemark() == null : this.getCourseRemark().equals(other.getCourseRemark()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getStuName() == null) ? 0 : getStuName().hashCode());
        result = prime * result + ((getCourseName() == null) ? 0 : getCourseName().hashCode());
        result = prime * result + ((getCourseTotalTime() == null) ? 0 : getCourseTotalTime().hashCode());
        result = prime * result + ((getCourseRestTime() == null) ? 0 : getCourseRestTime().hashCode());
        result = prime * result + ((getCourseLastTime() == null) ? 0 : getCourseLastTime().hashCode());
        result = prime * result + ((getCourseAdminGroupId() == null) ? 0 : getCourseAdminGroupId().hashCode());
        result = prime * result + ((getCourseOwnerUserId() == null) ? 0 : getCourseOwnerUserId().hashCode());
        result = prime * result + ((getCourseRemark() == null) ? 0 : getCourseRemark().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
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
                ", stu_name=" + stuName +
                ", course_name=" + courseName +
                ", course_total_time=" + courseTotalTime +
                ", course_rest_time=" + courseRestTime +
                ", course_last_time=" + courseLastTime +
                ", course_admin_group_id=" + courseAdminGroupId +
                ", course_owner_user_id=" + courseOwnerUserId +
                ", course_remark=" + courseRemark +
                ", is_delete=" + isDelete +
                ", create_time=" + createTime +
                ", update_time=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}