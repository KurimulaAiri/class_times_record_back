package com.shiroko.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 查询学生DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/4/24 上午1:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryStudentDTO {

    private Long id;

    private Long parentId;

    private Long pageSize;

    private Long currentPage;

}
