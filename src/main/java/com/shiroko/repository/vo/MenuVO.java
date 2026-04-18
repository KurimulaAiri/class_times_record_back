package com.shiroko.repository.vo;

import lombok.Data;

/**
 * Description: 菜单VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 下午3:30
 */
@Data
public class MenuVO {
    private Long id;
    private String menuName;
    private String icon;
    private Long iconType;
    private String bgColor;
    private String path;
    private Long sortOrder;
    private Boolean isVisible;
    private String createTimeStr;
    private String updateTimeStr;
}
