package com.shiroko.repository.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * Description: 菜单实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 上午15:21
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="menu")
@Data
public class Menu extends BaseEntity {
    /**
     * 菜单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;

    /**
     * 菜单图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 图标路径类型
     * 0：uni-app内置图标（icon内容直接填写uni-app对应图标名称）
     * 1：独立图标路径（icon内容填写前端vue路径，直接填写到图片src的内容）
     */
    @TableField(value = "icon_type")
    private Long iconType;

    /**
     * 图标背景颜色(Hex码)
     */
    @TableField(value = "bg_color")
    private String bgColor;

    /**
     * 跳转路由路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 排序权值(越小越靠前)
     */
    @TableField(value = "sort_order")
    private Long sortOrder;

    /**
     * 是否显示: 1显示, 0隐藏
     */
    @TableField(value = "is_visible")
    private Boolean isVisible;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}