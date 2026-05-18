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
 * Description: 班级与老师的关联表实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/18 上午10:21
 */
@TableName(value = "class_teacher")
@Data
public class ClassTeacher implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 班级老师表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 班级id
     */
    private Long classId;
    /**
     * 老师id
     */
    private Long teacherId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}