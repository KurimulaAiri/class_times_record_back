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
 * Description: 菜单实体类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 上午15:21
 */
@TableName(value ="menu")
@Data
public class Menu implements Serializable {
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
     * 0：uniapp内置图标（icon内容直接填写uniapp对应图标名称）
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
        Menu other = (Menu) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getIconType() == null ? other.getIconType() == null : this.getIconType().equals(other.getIconType()))
            && (this.getBgColor() == null ? other.getBgColor() == null : this.getBgColor().equals(other.getBgColor()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getSortOrder() == null ? other.getSortOrder() == null : this.getSortOrder().equals(other.getSortOrder()))
            && (this.getIsVisible() == null ? other.getIsVisible() == null : this.getIsVisible().equals(other.getIsVisible()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getIconType() == null) ? 0 : getIconType().hashCode());
        result = prime * result + ((getBgColor() == null) ? 0 : getBgColor().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getSortOrder() == null) ? 0 : getSortOrder().hashCode());
        result = prime * result + ((getIsVisible() == null) ? 0 : getIsVisible().hashCode());
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
                ", menuName=" + menuName +
                ", icon=" + icon +
                ", iconType=" + iconType +
                ", bgColor=" + bgColor +
                ", path=" + path +
                ", sortOrder=" + sortOrder +
                ", isVisible=" + isVisible +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}