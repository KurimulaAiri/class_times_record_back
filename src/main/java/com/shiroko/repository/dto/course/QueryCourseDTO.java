package com.shiroko.repository.dto.course;

import com.shiroko.repository.dto.course.validategroup.QueryGroup;
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

    @NotNull(message = "学生ID不能为空", groups = QueryGroup.ByStudentId.class)
    private Long studentId;

    @NotNull(message = "机构ID不能为空", groups = QueryGroup.ByInstitutionId.class)
    private Long institutionId;

    private String keyword;

    @NotNull(message = "当前页不能为空", groups = {QueryGroup.ByInstitutionId.class, QueryGroup.ByStudentId.class})
    private Long currentPage;
    @NotNull(message = "每页数量不能为空", groups = {QueryGroup.ByInstitutionId.class, QueryGroup.ByStudentId.class})
    private Long pageSize;
}
