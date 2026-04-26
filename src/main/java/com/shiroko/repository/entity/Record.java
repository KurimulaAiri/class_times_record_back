package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * Description: 记录实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/22 下午22:50
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="record")
@Data
@Accessors(chain = true)
public class Record extends BaseEntity {
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
}