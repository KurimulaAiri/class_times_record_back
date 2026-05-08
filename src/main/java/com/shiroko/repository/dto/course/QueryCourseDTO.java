package com.shiroko.repository.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Description: 查询课程DTO类
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/9 上午12:36
 */
@Data
public class QueryCourseDTO {
    @NotNull(message = "机构ID不能为空")
    private Long institutionId;
    @NotNull(message = "当前页不能为空")
    private Long currentPage;
    @NotNull(message = "每页数量不能为空")
    private Long pageSize;
}
