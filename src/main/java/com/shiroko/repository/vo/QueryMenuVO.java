package com.shiroko.repository.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 查询菜单VO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 下午11:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryMenuVO {
    private List<MenuVO> menus;
    private Long total;
}
