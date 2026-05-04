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
 * Description: 机构实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/4 下午16:43
 */
@TableName(value ="institution")
@Data
public class Institution implements Serializable {
    /**
     * 机构id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 机构名称
     */
    private String institutionName;

    /**
     * 机构地址
     */
    private String institutionAddress;

    /**
     * 机构状态：
0：待审核；
1：启用；
2：禁用；
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}