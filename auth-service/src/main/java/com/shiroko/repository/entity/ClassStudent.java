package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Description: 班级学生实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午8:55
 */
@Data
public class ClassStudent implements Serializable {

    /**
     * 班级学生表id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 班级id
     */
    @TableField("class_id")
    private Long classId;

    /**
     * 学生id
     */
    @TableField("student_id")
    private Long studentId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
