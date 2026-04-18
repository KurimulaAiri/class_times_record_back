package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 查询菜单DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/18 下午3:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryMenuDTO {
    private Long roleId;
    private Long currentPage;
    private Long pageSize;
}
